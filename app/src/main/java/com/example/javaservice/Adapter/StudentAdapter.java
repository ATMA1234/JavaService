package com.example.javaservice.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.javaservice.Model.Student;
import com.example.javaservice.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.Payments_ViewHolder> {
    private ArrayList<Student> arrayList;
    private Context context;

    public StudentAdapter(ArrayList<Student> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public Payments_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter, parent, false);
        Payments_ViewHolder viewHolder = new Payments_ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Payments_ViewHolder holder, int i) {
        holder.tv_slno.setText(arrayList.get(i).getId());
        holder.tv_name.setText(arrayList.get(i).getName());
        holder.tv_age.setText(arrayList.get(i).getAge());
        holder.tv_location.setText(arrayList.get(i).getPlace());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Payments_ViewHolder extends ViewHolder {
        TextView tv_slno, tv_name, tv_age, tv_location;

        private Payments_ViewHolder(View itemView) {
            super(itemView);
            tv_slno = itemView.findViewById(R.id.txt_id);
            tv_name = itemView.findViewById(R.id.txt_name);
            tv_age = itemView.findViewById(R.id.txt_age);
            tv_location = itemView.findViewById(R.id.txt_location);
        }
    }
}
