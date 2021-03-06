package com.syl.mingkie.simplepocketaccount.ArrayAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.syl.mingkie.simplepocketaccount.Data.Month;
import com.syl.mingkie.simplepocketaccount.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * This class is used for yearly summary activity to display a list of months.
 */
public class YearlySummaryAdapter extends ArrayAdapter<Month> {
    private Context context;
    private List<Month> data;

    public YearlySummaryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Month> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.listview_yearly_summary, parent, false);
        TextView month = (TextView) view.findViewById(R.id.monthYearlyListView);
        TextView income = (TextView) view.findViewById(R.id.incomeYearlyListView);
        TextView expense = (TextView) view.findViewById(R.id.expenseYearlySummary);
        // Month
        month.setText(data.get(position).getMonthString());
        // Truncate double
        Double truncatedIncomeAmount = BigDecimal.valueOf(data.get(position).getIncome().getTotalAmount()).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedExpenseAmount = BigDecimal.valueOf(data.get(position).getExpense().getTotalAmount()).setScale(3, RoundingMode.HALF_UP).doubleValue();
        // Amount
        income.setText(truncatedIncomeAmount + "");
        expense.setText(truncatedExpenseAmount + "");
        return view;
    }
}
