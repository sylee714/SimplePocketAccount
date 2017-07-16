package com.example.mingkie.simplepocketaccount.ArrayAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mingkie.simplepocketaccount.Data.Month;
import com.example.mingkie.simplepocketaccount.R;

import java.util.List;

/**
 * Created by MingKie on 7/13/2017.
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

        month.setText(data.get(position).getMonthString());
        income.setText(data.get(position).getIncome().getTotalAmount() + "");
        expense.setText(data.get(position).getExpense().getTotalAmount() + "");


        return view;
    }
}
