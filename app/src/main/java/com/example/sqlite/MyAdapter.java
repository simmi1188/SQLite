package com.example.sqlite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<student> studentList;
    private View.OnClickListener onClickListener;

    public MyAdapter(Context context, View.OnClickListener onClickListener) {
        this.context = context;
        this.studentList = new ArrayList<>();
        this.onClickListener=onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.student_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        student student=studentList.get(i);
        myViewHolder.txtId.setText(Integer.toString(student.getID()));
        myViewHolder.txtName.setText(student.getName());
        myViewHolder.txtMarks.setText(student.getMarks());

        myViewHolder.imgDelete.setTag(student);
        myViewHolder.imgEdit.setTag(student);


        myViewHolder.imgDelete.setOnClickListener(onClickListener);
        myViewHolder.imgEdit.setOnClickListener(onClickListener);
    }

    public ArrayList<student> getStudentList() {
        return studentList;
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtId, txtName, txtMarks;
        public ImageView imgDelete, imgEdit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.textid);
            txtName = (TextView) itemView.findViewById(R.id.textname);
            txtMarks = (TextView) itemView.findViewById(R.id.marks);
            imgDelete = (ImageView) itemView.findViewById(R.id.delete);
            imgEdit = (ImageView) itemView.findViewById(R.id.edit);

        }
    }
}
