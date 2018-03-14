package adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.kft.mfs.R;
import data.TransactionLog;
import java.util.List;



public class TransactionLogListAdapter extends BaseAdapter {

  public List<TransactionLog> mTransactionList;
  public Context context;
  public OnTransactionItemDetailListener onTransactionItemDetailListener;

  public TransactionLogListAdapter(Context context, List<TransactionLog> mTransactionList,OnTransactionItemDetailListener onTransactionItemDetailListener )
  {
    this.context = context;
    this.mTransactionList = mTransactionList;
    this.onTransactionItemDetailListener = onTransactionItemDetailListener;
  }

  @Override
  public int getCount() {
    return mTransactionList.size();
  }

  @Override
  public TransactionLog getItem(int position) {
    return mTransactionList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getViewTypeCount() {
    // menu type count
    return 3;
  }

  @Override
  public int getItemViewType(int position) {
    // current menu type
    return position % 3;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = View.inflate(context.getApplicationContext(),R.layout.swipe_item_list, null);
      new ViewHolder(convertView);
    }
    ViewHolder holder = (ViewHolder) convertView.getTag();
    final TransactionLog item = getItem(position);
    Log.e("Adapter","Pos: "+position+"Item: "+item.toString());
    //holder.iv_icon.setImageDrawable();
    holder.tv_merchant_name.setText("Merchant: "+item.getMerchantName());
    holder.tv_amount.setText("Amount: "+Integer.toString(item.getAmount())+" \u200E৳");
    holder.tv_date.setText("Date: "+item.getDate());
    convertView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        onTransactionItemDetailListener.onTransactionItemClick(item);
      }
    });
    return convertView;
  }

  class ViewHolder {
    ImageView iv_icon;
    TextView tv_merchant_name;
    TextView tv_amount;
    TextView tv_date;

    public ViewHolder(View view) {
      iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
      tv_merchant_name = (TextView) view.findViewById(R.id.tv_merchant_name);
      tv_amount = (TextView) view.findViewById(R.id.tv_amount);
      tv_date = (TextView) view.findViewById(R.id.tv_date);
      view.setTag(this);
    }
  }


  public int dp2px(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        context.getResources().getDisplayMetrics());
  }

  public interface OnTransactionItemDetailListener{
    void onTransactionItemClick(TransactionLog transactionLog);
  }
}
