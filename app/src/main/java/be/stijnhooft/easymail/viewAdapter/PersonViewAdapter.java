package be.stijnhooft.easymail.viewAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import be.stijnhooft.easymail.R;
import be.stijnhooft.easymail.model.Person;
import be.stijnhooft.easymail.service.internal.OnSelectPersonListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonViewAdapter extends RecyclerView.Adapter<PersonViewAdapter.ViewHolder> {

    private final OnSelectPersonListener onSelectPersonListener;
    private List<Person> persons;
    private Context context;

    public PersonViewAdapter(Context context, List<Person> persons, OnSelectPersonListener onSelectPersonListener) {
        this.persons = persons;
        this.context = context;
        this.onSelectPersonListener = onSelectPersonListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d("RecycleViewAdapter", "onBindViewHolder: called.");

        Person person = persons.get(position);
        Glide.with(context)
                .asBitmap()
                .load(person.getImage())
                .into(viewHolder.image);

        if (person.hasNewMessages()) {
            viewHolder.image.setBorderWidth(10);
            viewHolder.image.setBorderColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            viewHolder.image.setBorderWidth(0);
        }

        viewHolder.parentLayout.setOnClickListener(l -> onSelectPersonListener.onSelectPerson(person));
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView image;
        private LinearLayout parentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

    }
}
