package pt.ptinovacao.arqospocket.views.settings;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import java.util.Locale;

import pt.ptinovacao.arqospocket.R;

public class LanguageListArrayAdapter extends ArrayAdapter<Locale> {

    private Locale[] locales;

    private Activity context;

    private int selected;

    public LanguageListArrayAdapter(Activity context, Locale[] list, int selected) {
        super(context, R.layout.language_list_row, list);
        this.context = context;
        this.locales = list;
        this.selected = selected;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.language_list_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (CheckedTextView) view.findViewById(R.id.name);
            viewHolder.flag = (ImageView) view.findViewById(R.id.flag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(locales[position].getDisplayName());
        holder.flag.setBackgroundResource(
                ResourceLanguage.getRadioInformationParameter(locales[position].getCountry()).getResource());

        if (position == selected) {
            holder.name.setChecked(true);
        } else {
            holder.name.setChecked(false);
        }

        return view;
    }

    enum ResourceLanguage {
        EN("", R.drawable.flag_en),
        PT("PT", R.drawable.flag_pt),
        FR("FR", R.drawable.flag_fr);

        private String key;

        private int resource;

        ResourceLanguage(String key, int resource) {
            this.key = key;
            this.resource = resource;
        }

        public static ResourceLanguage getRadioInformationParameter(String key) {
            for (ResourceLanguage b : ResourceLanguage.values()) {
                if (b.getKey().equals(key)) {
                    return b;
                }
            }
            return EN;
        }

        public String getKey() {
            return key;
        }

        public int getResource() {
            return resource;
        }

        public void setResource(int resource) {
            this.resource = resource;
        }
    }

    static class ViewHolder {

        protected CheckedTextView name;
        protected ImageView flag;
    }
}
