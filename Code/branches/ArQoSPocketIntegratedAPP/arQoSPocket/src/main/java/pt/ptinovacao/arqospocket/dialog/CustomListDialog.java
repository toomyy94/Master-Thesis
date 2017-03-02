package pt.ptinovacao.arqospocket.dialog;

import pt.ptinovacao.arqospocket.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CustomListDialog extends Dialog {

	protected Context ctx;
	protected ListView list;
//	private List<Instruction_Entry> instructions;
//	private ArrayAdapter adapter;
	
	public CustomListDialog(Context context, int view, ArrayAdapter<?> adapter) {
		super(context);
		this.ctx = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(view);
		
		/* To stretch the dialog in width - FILL_PARENT */
		/*
		 getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		 getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		 getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
		 */		
		
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
	}
	
//	private ArrayList<Instruction_Entry> getInstructionsList() {
//		ArrayList<Instruction_Entry> instructions = new ArrayList<Instruction_Entry>();
//		instructions.add(new Instruction_Entry(R.string.pending_tests_all, R.drawable.icon_testes));
//		instructions.add(new Instruction_Entry(R.string.pending_tests_mobile, R.drawable.icon_testes));
//		instructions.add(new Instruction_Entry(R.string.pending_tests_wifi, R.drawable.icon_testes));
//		instructions.add(new Instruction_Entry(R.string.pending_tests_both, R.drawable.icon_testes));
//		
//		return instructions;
//	}

}
