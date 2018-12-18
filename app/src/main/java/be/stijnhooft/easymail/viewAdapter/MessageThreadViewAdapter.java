package be.stijnhooft.easymail.viewAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import be.stijnhooft.easymail.R;
import be.stijnhooft.easymail.model.Mail;

public class MessageThreadViewAdapter extends RecyclerView.Adapter<MessageThreadViewAdapter.ViewHolder> {

    private List<Mail> messages;
    private Context context;

    public MessageThreadViewAdapter(Context context, List<Mail> messages) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_thread, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Mail mail = messages.get(position);
        viewHolder.message.setText(mail.getMessage());
        if (mail.isIncoming()) {
            viewHolder.message.setBackground(ContextCompat.getDrawable(context, R.drawable.incoming_bubble));
        } else {
            viewHolder.message.setBackground(ContextCompat.getDrawable(context, R.drawable.outgoing_bubble));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView sender;
        private TextView message;
        private LinearLayout parentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            parentLayout = itemView.findViewById(R.id.message_thread_parent_layout);
        }

    }
}
