package pt.ptinovacao.arqospocket.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import java.util.List;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.LanguageSelectionActivity;


public class LanguageListArrayAdapter extends ArrayAdapter<LanguageSelectionActivity.Language> {

    private final List<LanguageSelectionActivity.Language> list;
    private final Activity context;

    static class ViewHolder {
        protected CheckedTextView name;
        protected ImageView flag;
    }

    public LanguageListArrayAdapter(Activity context, List<LanguageSelectionActivity.Language> list) {
        super(context, R.layout.language_list_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.language_list_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (CheckedTextView) view.findViewById(R.id.name);
            viewHolder.flag = (ImageView) view.findViewById(R.id.flag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.flag.setImageDrawable(list.get(position).getFlag());

        return view;
    }
}
