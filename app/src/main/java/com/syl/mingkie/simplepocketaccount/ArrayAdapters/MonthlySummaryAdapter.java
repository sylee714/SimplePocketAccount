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

import com.syl.mingkie.simplepocketaccount.Data.Week;
import com.syl.mingkie.simplepocketaccount.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * This class is used for monthly summary activity to display a list of weeks.
 */
public class MonthlySummaryAdapter extends ArrayAdapter<Week> {
    private Context context;
    private List<Week> data;

    public MonthlySummaryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Week> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.listview_monthly_summary, parent, false);
        TextView week = (TextView) view.findViewById(R.id.weekMonthlyListView);
        TextView income = (TextView) view.findViewById(R.id.incomeMonthlyListView);
        TextView expense = (TextView) view.findViewById(R.id.expenseMonthlyListView);
        // Week#
        week.setText("Week " + data.get(position).getWeekOfMonth());
        // Truncate double
        Double truncatedIncomeAmount = BigDecimal.valueOf(data.get(position).getIncome().getTotalAmount()).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedExpenseAmount = BigDecimal.valueOf(data.get(position).getExpense().getTotalAmount()).setScale(3, RoundingMode.HALF_UP).doubleValue();
        // Amounts
        income.setText(truncatedIncomeAmount + "");
        expense.setText(truncatedExpenseAmount + "");
        return view;
    }
}
