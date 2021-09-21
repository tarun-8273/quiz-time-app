package goyal.world.quiztime.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import goyal.world.quiztime.Models.QuestionModal;
import goyal.world.quiztime.R;

import static goyal.world.quiztime.DbQuery.ANSWERED;
import static goyal.world.quiztime.DbQuery.REVIEW;
import static goyal.world.quiztime.DbQuery.UNANSWERED;
import static goyal.world.quiztime.DbQuery.g_quesList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<QuestionModal> questionsList;

    //create a constructor for que list
    public QuestionsAdapter(List<QuestionModal> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(i);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ques;
        private Button optionA, optionB, optionC, optionD, prevSelectedB;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ques=itemView.findViewById(R.id.tv_question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);

            prevSelectedB = null;

        }

        private void setData(final int pos)
        {
            ques.setText(questionsList.get(pos).getQuestion());
            optionA.setText(questionsList.get(pos).getOptionA());
            optionB.setText(questionsList.get(pos).getOptionB());
            optionC.setText(questionsList.get(pos).getOptionC());
            optionD.setText(questionsList.get(pos).getOptionD());

            setOption(optionA, 1, pos);
            setOption(optionB, 2, pos);
            setOption(optionC, 3, pos);
            setOption(optionD, 4, pos);

            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectOption(optionA, 1, pos);
                }
            });

            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectOption(optionB, 2, pos);
                }
            });

            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectOption(optionC, 3, pos);
                }
            });

            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectOption(optionD, 4, pos);
                }
            });
        }

        private void selectOption(Button btn, int option_num, int queID)
        {

            if(prevSelectedB == null)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
                g_quesList.get(queID).setSelectedAns(option_num);

                changeStatus(queID, ANSWERED);
                prevSelectedB = btn;
            }
            else
            {

                if(prevSelectedB.getId() == btn.getId())
                {
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    g_quesList.get(queID).setSelectedAns(-1);

                    changeStatus(queID,UNANSWERED);
                    prevSelectedB = null;
                }
                else
                {
                    prevSelectedB.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);

                    g_quesList.get(queID).setSelectedAns(option_num);

                    changeStatus(queID, ANSWERED);
                    prevSelectedB = btn;
                }

            }

        }

        private void changeStatus(int id, int status)
        {
            if( g_quesList.get(id).getStatus() != REVIEW)
            {
                g_quesList.get(id).setStatus(status);
            }
        }

        private void setOption(Button btn, int option_num, int queID)
        {
            if(g_quesList.get(queID).getSelectedAns() == option_num)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
            }
            else
            {
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }
        }
    }


}
