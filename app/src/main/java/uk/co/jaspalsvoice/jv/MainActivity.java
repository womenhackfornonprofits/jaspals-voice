package uk.co.jaspalsvoice.jv;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import uk.co.jaspalsvoice.jv.activities.AboutMeActivity;
import uk.co.jaspalsvoice.jv.activities.AllergiesActivity;
import uk.co.jaspalsvoice.jv.activities.BaseActivity;
import uk.co.jaspalsvoice.jv.activities.DiagnosesActivity;
import uk.co.jaspalsvoice.jv.activities.FontSizeActivity;
import uk.co.jaspalsvoice.jv.activities.FoodAllergiesActivity;
import uk.co.jaspalsvoice.jv.activities.GpActivity;
import uk.co.jaspalsvoice.jv.activities.LikesDislikesActivity;
import uk.co.jaspalsvoice.jv.activities.MedicinesActivity;
import uk.co.jaspalsvoice.jv.activities.PersonalDetailsActivity;
import uk.co.jaspalsvoice.jv.activities.TermsAndConditionsActivity;
import uk.co.jaspalsvoice.jv.activities.VitalsActivity;
import uk.co.jaspalsvoice.jv.db.DatabaseHelper;
import uk.co.jaspalsvoice.jv.task.FetchWordsTask;
import uk.co.jaspalsvoice.jv.task.HandlerConstants;
import uk.co.jaspalsvoice.jv.task.InitWordsTask;
import uk.co.jaspalsvoice.jv.task.InsertWordTask;
import uk.co.jaspalsvoice.jv.task.KeypadListener;

public class MainActivity extends BaseActivity implements SuggestionsAdapter.Listener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static boolean t9Enabled = true;

    private String defaultSuggestion;
    private String savedTextMessage = "";
    private boolean isPassportScene;
    private Scene keypadScene;
    private Scene passportScene;

    private ViewGroup mSceneRoot;
    private TransitionManager mTxManager;

    private TextView t9View;

    private TextView currentTextCaseView;
    private EditText messageTextView;
    private TextToSpeech textToSpeech;
    private ImageButton ttsTextView;
    private int messageTextViewLocation[] = new int[2];

    private Layout messageTextLayout;

    private PopupWindow popupWindow;
    private ListView suggestionsListView;

    //    private KeypadHandler handler;
//    private T9Handler t9Handler;
    private Handler currentHandler;
    private ShareActionProvider shareActionProvider;
    private Intent shareIntent = new Intent(Intent.ACTION_SEND);
    private SuggestionsAdapter suggestionsAdapter;

    private TrieT9 trieT9 = new TrieT9();

    private FetchWordsTask.OnResultsListener onResultsListener = new FetchWordsTask.OnResultsListener() {
        @Override
        public void onUpdateUi(List<String> text) {
//            suggestions0View.setText(text.size() >= 1 ? text.get(0) : defaultSuggestion);
//            suggestions1View.setText(text.size() >= 2 ? text.get(1) : defaultSuggestion);
//            suggestions2View.setText(text.size() >= 3 ? text.get(2) : defaultSuggestion);
//            suggestions3View.setText(text.size() >= 4 ? text.get(3) : defaultSuggestion);
        }
    };

    private View.OnClickListener onSuggestionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((T9Handler) currentHandler).setListener(keyPadListener);
            if (!t9Enabled){
                t9View.setBackgroundColor(ContextCompat.getColor
                        (MainActivity.this, android.R.color.holo_green_dark));
            } else {
                t9View.setBackgroundColor(ContextCompat.getColor
                        (MainActivity.this, android.R.color.darker_gray));
            }
            t9Enabled = !t9Enabled;
        }
    };


    private View.OnClickListener changeCaseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentHandler.sendEmptyMessage(HandlerConstants.SET_TEXT_CASE);
        }
    };

    private View.OnClickListener clearScreenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetHandlerState(HandlerConstants.CLEAR_MESSAGE, currentHandler);
            messageTextView.setText(null);
            showCursor();
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        }
    };

    private View.OnClickListener deleteLetterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetHandlerState(HandlerConstants.CLEAR_LETTER, currentHandler);
            String text = messageTextView.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                messageTextView.setText(text.substring(0, text.length() - 1));
                showCursor();
            }
        }
    };

    private ActionMode.Callback selectionCallback = new ActionMode.Callback() {
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.removeItem(android.R.id.selectAll);
            menu.removeItem(android.R.id.cut);
            menu.removeItem(android.R.id.copy);
            menu.removeItem(android.R.id.paste);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                menu.removeItem(android.R.id.replaceText);
                menu.removeItem(android.R.id.shareText);
            }
            menu.add(0, 0, 0, "Add to dictionary").setIcon(R.drawable.add_to_dictionary);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case 0:
                    int min = 0;
                    int max = messageTextView.getText().length();
                    if (messageTextView.isFocused()) {
                        final int selStart = messageTextView.getSelectionStart();
                        final int selEnd = messageTextView.getSelectionEnd();

                        min = Math.max(0, Math.min(selStart, selEnd));
                        max = Math.max(0, Math.max(selStart, selEnd));
                    }
                    final CharSequence selectedText = messageTextView.getText().subSequence(min, max);
                    Log.i(TAG, "onActionItemClicked: selected text = " + selectedText);
                    trieT9.add(encodeWord(selectedText.toString()), selectedText.toString());
                    new InsertWordTask(new WeakReference<Context>(MainActivity.this)).execute(selectedText.toString());
                    mode.finish();
                    // return true;
                default:
                    break;
            }
            return false;
        }

    };

    private KeypadListener keyPadListener = new KeypadListener() {
        @Override
        public void onAppendMessage(String msg) {
            messageTextView.append(msg);
            showCursor();
        }

        @Override
        public void onAppendMessage(Spannable msg) {
            messageTextView.append(msg);
            showCursor();
        }

        @Override
        public String getMessage() {
            return messageTextView.getText().toString();
        }

        @Override
        public void setMessage(String msg) {
            messageTextView.setText(msg);
            showCursor();
        }

        @Override
        public void clearSuggestions() {
//            suggestions0View.setText(defaultSuggestion);
//            suggestions1View.setText(defaultSuggestion);
//            suggestions2View.setText(defaultSuggestion);
//            suggestions3View.setText(defaultSuggestion);
        }

        @Override
        public void setCurrentCase(String textCase) {
            currentTextCaseView.setText(textCase);
        }

        @Override
        public void fetchWords(String text) {
            // new FetchWordsTask(getApplicationContext(), onResultsListener).execute(text);
            List<String> list = new ArrayList<>();
            if (!t9Enabled) {
                Cursor loadWordsCursor = DatabaseHelper.getInstance(MainActivity.this).
                        loadWords(MainActivity.this, text);
                if (loadWordsCursor != null) {
                    loadWordsCursor.moveToFirst();
                    while (!loadWordsCursor.isAfterLast()) {
                        list.add(loadWordsCursor.getString(loadWordsCursor.getColumnIndex("word")));
                        loadWordsCursor.moveToNext();
                    }
                }
            } else {
                list = trieT9.search(encodeWord(text.toLowerCase()));
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Log.i(TAG, "fetchWords: " + list.get(i));
                    }
                }
            }

            Log.i(TAG, "fetchWords: text = " + text);


            final int pos = messageTextView.getSelectionStart();
            messageTextLayout = messageTextView.getLayout();
            messageTextView.getLocationOnScreen(messageTextViewLocation);
            Log.i(TAG, "fetchWords: location = " + messageTextViewLocation[0] + " " + messageTextViewLocation[1]);
            int line = messageTextLayout.getLineForOffset(pos);
            int baseline = messageTextLayout.getLineBaseline(line);
            int ascent = messageTextLayout.getLineAscent(line);
            float x = messageTextLayout.getPrimaryHorizontal(pos);
            float y = baseline + ascent;

            suggestionsAdapter.setData(list);

            if (popupWindow == null) {
                popupWindow = new PopupWindow(suggestionsListView, 300, 550, false);
            } else {
                popupWindow.dismiss();
            }
            popupWindow.showAtLocation(suggestionsListView, Gravity.TOP | Gravity.LEFT, (int) x + messageTextViewLocation[0] + 50, (int) y + messageTextViewLocation[1]);
        }
    };

    private TextWatcher messageWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No action.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No action.
        }

        @Override
        public void afterTextChanged(Editable s) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, s.toString());
            if (TextUtils.isEmpty(s)) {
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
            if (shareActionProvider != null) {
                shareActionProvider.setShareIntent(shareIntent);
            }
        }
    };

    private View.OnClickListener ttsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String toSpeak = messageTextView.getText().toString();
            Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
            textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
        }

    };

    private static HashMap<Integer, Set<Character>> keyboard;
    private static Map<Character, Integer> charMapping;

    private static void initMap() {
        if (keyboard != null && charMapping != null) {
            return;
        }
        keyboard = new HashMap<>();
        charMapping = new HashMap<>();

        HashSet<Character> keys = new HashSet<>();
        keys.add('a');
        keys.add('b');
        keys.add('c');

        keys.add('A');
        keys.add('B');
        keys.add('C');
        keyboard.put(2, keys);

        charMapping.put('a', 2);
        charMapping.put('b', 2);
        charMapping.put('c', 2);

        charMapping.put('A', 2);
        charMapping.put('B', 2);
        charMapping.put('C', 2);


        keys = new HashSet<>();
        keys.add('d');
        keys.add('e');
        keys.add('f');

        keys.add('D');
        keys.add('E');
        keys.add('F');
        keyboard.put(3, keys);

        charMapping.put('d', 3);
        charMapping.put('e', 3);
        charMapping.put('f', 3);

        charMapping.put('D', 3);
        charMapping.put('E', 3);
        charMapping.put('F', 3);


        keys = new HashSet<>();
        keys.add('g');
        keys.add('h');
        keys.add('i');

        keys.add('G');
        keys.add('H');
        keys.add('I');
        keyboard.put(4, keys);

        charMapping.put('g', 4);
        charMapping.put('h', 4);
        charMapping.put('i', 4);

        charMapping.put('G', 4);
        charMapping.put('H', 4);
        charMapping.put('I', 4);

        keys = new HashSet<>();
        keys.add('j');
        keys.add('k');
        keys.add('l');

        keys.add('J');
        keys.add('K');
        keys.add('L');

        keyboard.put(5, keys);

        charMapping.put('j', 5);
        charMapping.put('k', 5);
        charMapping.put('l', 5);

        charMapping.put('J', 5);
        charMapping.put('K', 5);
        charMapping.put('L', 5);


        keys = new HashSet<>();
        keys.add('m');
        keys.add('n');
        keys.add('o');

        keys.add('M');
        keys.add('N');
        keys.add('O');
        keyboard.put(6, keys);

        charMapping.put('m', 6);
        charMapping.put('n', 6);
        charMapping.put('o', 6);

        charMapping.put('M', 6);
        charMapping.put('N', 6);
        charMapping.put('O', 6);

        keys = new HashSet<>();
        keys.add('p');
        keys.add('q');
        keys.add('r');
        keys.add('s');

        keys.add('P');
        keys.add('Q');
        keys.add('R');
        keys.add('S');
        keyboard.put(7, keys);

        charMapping.put('p', 7);
        charMapping.put('q', 7);
        charMapping.put('r', 7);
        charMapping.put('s', 7);

        charMapping.put('P', 7);
        charMapping.put('Q', 7);
        charMapping.put('R', 7);
        charMapping.put('S', 7);


        keys = new HashSet<>();
        keys.add('t');
        keys.add('u');
        keys.add('v');

        keys.add('T');
        keys.add('U');
        keys.add('V');
        keyboard.put(8, keys);

        charMapping.put('t', 8);
        charMapping.put('u', 8);
        charMapping.put('v', 8);

        charMapping.put('T', 8);
        charMapping.put('U', 8);
        charMapping.put('V', 8);

        keys = new HashSet<>();
        keys.add('w');
        keys.add('x');
        keys.add('y');
        keys.add('z');

        keys.add('W');
        keys.add('X');
        keys.add('Y');
        keys.add('Z');
        keyboard.put(9, keys);

        charMapping.put('w', 9);
        charMapping.put('x', 9);
        charMapping.put('y', 9);
        charMapping.put('z', 9);

        charMapping.put('W', 9);
        charMapping.put('X', 9);
        charMapping.put('Y', 9);
        charMapping.put('Z', 9);
    }

    private String encodeWord(String word) {
        initMap();
        StringBuilder encoded = new StringBuilder("");
        if (word != null) {
            for (int i = 0; i < word.length(); i++) {
                encoded.append(String.valueOf(charMapping.get(word.charAt(i))));
            }
        }

        return encoded.toString();
    }


    public TrieT9 getTrieT9() {
        return trieT9;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final JvPreferences preferences = ((JvApplication)getApplicationContext()).getPreferences();
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = preferences.getFontSize(); //0.85 small size, 1 normal size, 1,15 big etc

        //initiate the text-to-speech engine
        textToSpeech =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
        setContentView(R.layout.activity_main);

        setupScenes();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (!preferences.isFirstRun()) {
                    new InitWordsTask(getApplicationContext()).execute(getResources());
                    InputStream inputStream = getResources().openRawResource(R.raw.corncob_lowercase);
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    try {
                        while ((line = br.readLine()) != null) {
                            trieT9.add(encodeWord(line), line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "run: t9 loaded");
                    preferences.setFirstRunStatus(true);
                } else {
                    Cursor loadWordsCursor = DatabaseHelper.getInstance(MainActivity.this).
                            loadAllWords(MainActivity.this);
                    if (loadWordsCursor != null) {
                        loadWordsCursor.moveToFirst();
                        while (!loadWordsCursor.isAfterLast()) {
                            trieT9.add(
                                    encodeWord(loadWordsCursor.getString(loadWordsCursor.getColumnIndex("word"))),
                                    loadWordsCursor.getString(loadWordsCursor.getColumnIndex("word")));
                            loadWordsCursor.moveToNext();
                        }
                    }
                }
            }
        };

        (new Thread(r)).start();


        defaultSuggestion = getString(R.string.no_suggestion);
        shareIntent.setType("text/plain");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        if (t9Enabled) {
            currentHandler = new T9Handler(ContextCompat.getColor(this, R.color.colorAccent_40));
            ((T9Handler) currentHandler).setListener(keyPadListener);
        } else {
            currentHandler = new KeypadHandler(ContextCompat.getColor(this, R.color.colorAccent_40));
            ((KeypadHandler) currentHandler).setListener(keyPadListener);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Action 1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem appInfoItem = menu.findItem(R.id.menu_app_info);
        appInfoItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent appInfoIntent = new Intent(MainActivity.this, TermsAndConditionsActivity.class);

                //hack so that the back button works correctly
                //todo find a better fix
                isPassportScene = true;

                startActivity(appInfoIntent);
                return false;
            }
        });
        MenuItem item = menu.findItem(R.id.menu_text_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        MenuItem textSizeItem = menu.findItem(R.id.menu_text_font);
        textSizeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, FontSizeActivity.class);

                //hack so that the back button works correctly
                //todo find a better fix
                isPassportScene = true;

                startActivity(intent);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void resetHandlerState(int message, Handler h) {
        h.removeMessages(HandlerConstants.START_TIMER_MESSAGE_CODE);
        h.removeMessages(HandlerConstants.KEY_MESSAGE_WHAT);
        h.sendEmptyMessage(message);
    }

    private void showCursor() {
        messageTextView.setSelection(messageTextView.getText().length());
    }

    @Override
    public void onItemClicked(String text) {
        ((T9Handler) currentHandler).replaceWordWith(messageTextView.getText().toString(), text);
        popupWindow.dismiss();
    }

    private void setupScenes() {
        suggestionsAdapter = new SuggestionsAdapter(MainActivity.this, R.layout.suggestion, MainActivity.this);

        // Create the scene root for the scenes in this app
        mSceneRoot = (ViewGroup) findViewById(R.id.scene_root);

        // Create the scenes
        keypadScene = Scene.getSceneForLayout(mSceneRoot, R.layout.scene_keypad, this);
        keypadScene.setEnterAction(new Runnable() {
            @Override
            public void run() {
                isPassportScene = false;
                initKeypadListeners();
                initKeypadViews();
                onRestoreTextMessage();
            }
        });

        keypadScene.setExitAction(new Runnable() {
            @Override
            public void run() {
                onSaveTextMessage();
            }
        });

        passportScene = Scene.getSceneForLayout(mSceneRoot, R.layout.scene_passport, this);
        passportScene.setEnterAction(new Runnable() {
            @Override
            public void run() {
                isPassportScene = true;
                initPassportListeners();
            }
        });

        mTxManager = new TransitionManager();
        ChangeBounds transition = new ChangeBounds();
        transition.setDuration(600);
        Fade transitionFade = new Fade();
        transitionFade.setDuration(300);
        mTxManager.setTransition(keypadScene, passportScene, transition);
        mTxManager.setTransition(passportScene, keypadScene, transitionFade);
        keypadScene.enter();

    }

    @Override
    public void onBackPressed() {
        if (isPassportScene) {
            mTxManager.transitionTo(keypadScene);
        } else {
            super.onBackPressed();
        }
    }

    private void initKeypadListeners() {
        keypadScene.getSceneRoot().findViewById(R.id.one).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.two).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.three).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.four).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.five).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.six).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.seven).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.eight).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.nine).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.zero).setOnClickListener(keyClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.star).setOnClickListener(changeCaseClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.hash).setOnClickListener(hashClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.clear_screen).setOnClickListener(clearScreenClickListener);
        keypadScene.getSceneRoot().findViewById(R.id.delete_letter).setOnClickListener(deleteLetterClickListener);
    }

    private void initKeypadViews() {
        t9View = (TextView) keypadScene.getSceneRoot().findViewById(R.id.t_9);
        t9View.setActivated(t9Enabled);
        t9View.setOnClickListener(onSuggestionListener);

        currentTextCaseView = (TextView) keypadScene.getSceneRoot().findViewById(R.id.text_case);
        messageTextView = (EditText) keypadScene.getSceneRoot().findViewById(R.id.message_text);
        ttsTextView = (ImageButton) keypadScene.getSceneRoot().findViewById(R.id.text_to_speech);
        ttsTextView.setOnClickListener(ttsListener);

        messageTextView.setTextIsSelectable(true);
        messageTextView.setCursorVisible(true);
        messageTextView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
        messageTextView.setText("");
        showCursor();
        messageTextView.setCustomSelectionActionModeCallback(selectionCallback);
        messageTextView.addTextChangedListener(messageWatcher);

        View actionView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.suggestions_popup, (ViewGroup) findViewById(R.id.suggestions));
        suggestionsListView = (ListView) actionView;
        suggestionsListView.setAdapter(suggestionsAdapter);
    }

    private View.OnClickListener hashClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            mTxManager.transitionTo(passportScene);
        }
    };

    private View.OnClickListener keyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentHandler.removeMessages(HandlerConstants.START_TIMER_MESSAGE_CODE);
            Message keyMessage = Message.obtain();
            keyMessage.what = HandlerConstants.KEY_MESSAGE_WHAT;
            switch (v.getId()) {
                case R.id.one:
                    keyMessage.arg1 = 1;
                    break;
                case R.id.two:
                    keyMessage.arg1 = 2;
                    break;
                case R.id.three:
                    keyMessage.arg1 = 3;
                    break;
                case R.id.four:
                    keyMessage.arg1 = 4;
                    break;
                case R.id.five:
                    keyMessage.arg1 = 5;
                    break;
                case R.id.six:
                    keyMessage.arg1 = 6;
                    break;
                case R.id.seven:
                    keyMessage.arg1 = 7;
                    break;
                case R.id.eight:
                    keyMessage.arg1 = 8;
                    break;
                case R.id.nine:
                    keyMessage.arg1 = 9;
                    break;
                case R.id.zero:
                    keyMessage.arg1 = 0;
                    break;
            }
            currentHandler.sendMessage(keyMessage);
        }
    };

    private void initPassportListeners() {
        passportScene.getSceneRoot().findViewById(R.id.two).setOnClickListener(passportKeyListener);
        passportScene.getSceneRoot().findViewById(R.id.three).setOnClickListener(passportKeyListener);
        passportScene.getSceneRoot().findViewById(R.id.five).setOnClickListener(passportKeyListener);
        passportScene.getSceneRoot().findViewById(R.id.six).setOnClickListener(passportKeyListener);
        passportScene.getSceneRoot().findViewById(R.id.eight).setOnClickListener(passportKeyListener);
        passportScene.getSceneRoot().findViewById(R.id.nine).setOnClickListener(passportKeyListener);
        passportScene.getSceneRoot().findViewById(R.id.zero).setOnClickListener(passportKeyListener);
        passportScene.getSceneRoot().findViewById(R.id.hash).setOnClickListener(passportKeyListener);
        passportScene.getSceneRoot().findViewById(R.id.hash).setOnClickListener(passportKeyListener);
        passportScene.getSceneRoot().findViewById(R.id.vitals).setOnClickListener(passportKeyListener);
    }

    private View.OnClickListener passportKeyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.two:
                    startActivity(new Intent(MainActivity.this, PersonalDetailsActivity.class));
                    break;
                case R.id.three:
                    startActivity(new Intent(MainActivity.this, MedicinesActivity.class));
                    break;
                case R.id.five:
                    startActivity(new Intent(MainActivity.this, AllergiesActivity.class));
                    break;
                case R.id.six:
                    startActivity(new Intent(MainActivity.this, FoodAllergiesActivity.class));
                    break;
                case R.id.eight:
                    startActivity(new Intent(MainActivity.this, GpActivity.class));
                    break;
                case R.id.nine:
                    startActivity(new Intent(MainActivity.this, LikesDislikesActivity.class));
                    break;
                case R.id.zero:
                    startActivity(new Intent(MainActivity.this, DiagnosesActivity.class));
                    break;
                case R.id.hash:
                    startActivity(new Intent(MainActivity.this, AboutMeActivity.class));
                    break;

                case R.id.vitals:
                    startActivity(new Intent(MainActivity.this, VitalsActivity.class));
                    break;
            }
        }
    };

    private void onSaveTextMessage() {
        savedTextMessage = messageTextView.getText().toString();
    }

    private void onRestoreTextMessage() {
        messageTextView.setText(savedTextMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
