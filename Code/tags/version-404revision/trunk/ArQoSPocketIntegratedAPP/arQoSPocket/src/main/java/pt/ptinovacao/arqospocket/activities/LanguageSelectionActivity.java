package pt.ptinovacao.arqospocket.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.adapters.LanguageListArrayAdapter;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.util.LocaleHelper;

public class LanguageSelectionActivity extends ListActivity {

    public static String RESULT_LANGUAGE_CODE = "countrycode";
    public String[] languageNames, languageCodes;
    private TypedArray imgs;
    private List<Language> languageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int selectedPosition = populateLanguageList();
        ArrayAdapter<Language> adapter = new LanguageListArrayAdapter(this, languageList);
        setListAdapter(adapter);

        getListView().setItemsCanFocus(false);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setItemChecked(selectedPosition, true);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Language l = languageList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_LANGUAGE_CODE, l.getCode());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private int populateLanguageList() {
        int selectedLanguagePosition = 0;
        languageList = new ArrayList<Language>();
        languageNames = getResources().getStringArray(R.array.language_names);
        languageCodes = getResources().getStringArray(R.array.language_codes);
        imgs = getResources().obtainTypedArray(R.array.country_flags);
        String selectedLanguage = LocaleHelper.getLanguage(this);
        for(int i = 0; i < languageCodes.length; i++){
            languageList.add(new Language(languageNames[i], languageCodes[i], imgs.getDrawable(i)));
            if (selectedLanguage.equals(languageCodes[i])){
                selectedLanguagePosition = i;
            }
        }

        return selectedLanguagePosition;
    }

    public class Language {
        private String name;
        private String code;
        private Drawable flag;
        public Language(String name, String code, Drawable flag){
            this.name = name;
            this.code = code;
            this.flag = flag;
        }
        public String getName() {
            return name;
        }
        public Drawable getFlag() {
            return flag;
        }
        public String getCode() {
            return code;
        }

    }
}
