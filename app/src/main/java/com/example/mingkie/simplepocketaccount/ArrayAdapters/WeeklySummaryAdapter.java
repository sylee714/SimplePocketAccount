package com.example.mingkie.simplepocketaccount.ArrayAdapters;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mingkie.simplepocketaccount.Data.Action;
import com.example.mingkie.simplepocketaccount.Data.Day;
import com.example.mingkie.simplepocketaccount.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by MingKie on 7/13/2017.
 */

public class WeeklySummaryAdapter extends ArrayAdapter<Day> {
    private Context context;
    private List<Day> data;

    public WeeklySummaryAdapter(@NonNull Context context, @LayoutRes int resource,  @NonNull List<Day> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.listview_weekly_summary, parent, false);
        TextView day = (TextView) view.findViewById(R.id.dayWeeklyListView);
        TextView income = (TextView) view.findViewById(R.id.incomeWeeklyListView);
        TextView expense = (TextView) view.findViewById(R.id.expenseWeeklyListView);
        day.setText(data.get(position).getDay() + ", " + (data.get(position).getMonth() + 1) + "/" +
                data.get(position).getDayOfMonth() + "/" + data.get(position).getYear());
        Double truncatedIncomeAmount = BigDecimal.valueOf(data.get(position).getIncome().getTotalAmount()).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedExpenseAmount = BigDecimal.valueOf(data.get(position).getExpense().getTotalAmount()).setScale(3, RoundingMode.HALF_UP).doubleValue();
        income.setText(truncatedIncomeAmount + "");
        expense.setText(truncatedExpenseAmount + "");
        return view;
    }
}
