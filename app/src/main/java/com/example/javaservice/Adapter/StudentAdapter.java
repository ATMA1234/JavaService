package com.example.javaservice.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.javaservice.MainActivity;
import com.example.javaservice.Model.Student;
import com.example.javaservice.R;
import com.example.javaservice.ViewStudents;

import java.util.ArrayList;

import static com.example.javaservice.Values.Constants.DLG_DELETE;
import static com.example.javaservice.Values.Constants.DLG_UPDATE;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.Student_ViewHolder> {
    private ArrayList<Student> arrayList;
    private Context context;

    public StudentAdapter(ArrayList<Student> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Student_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter, parent, false);
        Student_ViewHolder viewHolder = new Student_ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Student_ViewHolder holder, int i) {
        holder.tv_slno.setText(arrayList.get(i).getId());
        holder.tv_name.setText(arrayList.get(i).getName());
        holder.tv_age.setText(arrayList.get(i).getAge());
        holder.tv_location.setText(arrayList.get(i).getPlace());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Student_ViewHolder extends ViewHolder implements View.OnClickListener {
        TextView tv_slno, tv_name, tv_age, tv_location, tv_update, tv_delete;

        private Student_ViewHolder(View itemView) {
            super(itemView);
            tv_slno = itemView.findViewById(R.id.txt_id);
            tv_name = itemView.findViewById(R.id.txt_name);
            tv_age = itemView.findViewById(R.id.txt_age);
            tv_location = itemView.findViewById(R.id.txt_location);
            tv_update = itemView.findViewById(R.id.txt_update);
            tv_update.setOnClickListener(this);
            tv_delete = itemView.findViewById(R.id.txt_delete);
            tv_delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            switch (v.getId()) {
                case R.id.txt_update:
                    ((ViewStudents) context).showDialoag(DLG_UPDATE, pos, arrayList);
                    break;

                case R.id.txt_delete:
                    ((ViewStudents) context).showDialoag(DLG_DELETE, pos, arrayList);
                    break;
            }
        }
    }
}
