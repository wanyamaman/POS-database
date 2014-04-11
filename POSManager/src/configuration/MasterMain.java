package configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wanyama.posmanager.R;

public class MasterMain extends Activity {

	private Context ctx;
	Button addStall;
	Button editStall;
	Button addProduct;
	Button editProduct;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.master_menu);
	    ctx = this;
	    
	    addStall = (Button) findViewById(R.id.addStallBtn);
	    editStall = (Button) findViewById(R.id.editStallBtn);
	    addProduct = (Button) findViewById(R.id.addProductBtn);
	    editProduct = (Button) findViewById(R.id.editProductBtn);
	    
	    addStall.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchAddStallActivity = new Intent(MasterMain.this, InsertStall.class);
				startActivity(launchAddStallActivity);
				
			}
		});
	    
	    editStall.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchEditStall = new Intent(MasterMain.this, EditStall.class);
				startActivity(launchEditStall);
				
			}
		});
	    
	    addProduct.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchAddProduct= new Intent(MasterMain.this, InsertProduct.class);
				startActivity(launchAddProduct);
				
			}
		});
	
	    editProduct.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchEditProduct = new Intent(MasterMain.this, EditProduct.class);
				startActivity(launchEditProduct);
				
			}
		});
	}

}
