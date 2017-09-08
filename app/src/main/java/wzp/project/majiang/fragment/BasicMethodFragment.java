package wzp.project.majiang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import wzp.project.majiang.R;
import wzp.project.majiang.widget.ListOptionPopupWindow;
import wzp.project.majiang.widget.ListOptionButton;
import wzp.project.majiang.widget.ShowFunctionTipPopupWindow;

/**
 * Created by wzp on 2017/8/29.
 */

public class BasicMethodFragment extends Fragment {

    private TextView tvPlayerNum;
    private Button btnChoosePlayerNum;
    private ListOptionButton btnChooseMajiangNum;
    private Button btnEastTop;


    private ShowFunctionTipPopupWindow pwShowFunTip;
    private ListOptionPopupWindow pwListOption;



    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_playerNum:
                    pwShowFunTip.setFunTip(R.string.player_num_fun_tip);
                    pwShowFunTip.showAsDropDown(tvPlayerNum);
                    break;

                case R.id.btn_choosePlayerNum:
                    pwListOption.showAsDropDown(btnChoosePlayerNum);
                    break;

                case R.id.btn_eastTop:
                    pwListOption.showAsDropDown(btnEastTop);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_method, container, false);

        initWidget(view);

        return view;
    }

    private void initWidget(View view) {
        tvPlayerNum = (TextView) view.findViewById(R.id.tv_playerNum);
        btnChoosePlayerNum = (Button) view.findViewById(R.id.btn_choosePlayerNum);
        btnChooseMajiangNum = (ListOptionButton) view.findViewById(R.id.btn_chooseMajiangNum);
        btnEastTop = (Button) view.findViewById(R.id.btn_eastTop);

        pwShowFunTip = new ShowFunctionTipPopupWindow(getContext());
        pwListOption = new ListOptionPopupWindow(getContext());
        pwListOption.setListViewItems(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});

        btnChooseMajiangNum.setListViewItems(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});

        tvPlayerNum.setOnClickListener(listener);
        btnChoosePlayerNum.setOnClickListener(listener);
        btnEastTop.setOnClickListener(listener);
    }


}