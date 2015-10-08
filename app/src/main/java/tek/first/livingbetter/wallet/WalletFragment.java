package tek.first.livingbetter.wallet;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import tek.first.livingbetter.R;
import tek.first.livingbetter.provider.DatabaseHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class WalletFragment extends Fragment {
    private RadioGroup switchRgWalletmain;
    private AnalysisFragment analysisFragment;
    private BudgetFragment budgetFragment;
    private HistoryFragment historyFragment;
    private FragmentTransaction transaction;
    private DatabaseHelper dataHelper;

    public WalletFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_wallet, container, false);

        switchRgWalletmain = (RadioGroup) view.findViewById(R.id.switch_rg_walletmain);
        dataHelper = new DatabaseHelper(getActivity());
        initData();
        if (null == budgetFragment) {
            budgetFragment = new BudgetFragment();
        }
        transaction = getFragmentManager().beginTransaction();
        transaction.remove(budgetFragment);
        budgetFragment = new BudgetFragment();
        transaction.replace(R.id.fragment_container, budgetFragment);
        transaction.commit();
        switchRgWalletmain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.switch_budget:
                        if (null == budgetFragment) {
                            budgetFragment = new BudgetFragment();
                        }
                        transaction = getFragmentManager().beginTransaction();
                        transaction.remove(budgetFragment);
                        budgetFragment = new BudgetFragment();
                        transaction.replace(R.id.fragment_container, budgetFragment);
                        transaction.commit();
                        break;

                    case R.id.switch_history:
                        if (null == historyFragment) {
                            historyFragment = new HistoryFragment();
                        }
                        transaction = getFragmentManager().beginTransaction();
                        transaction.remove(historyFragment);
                        historyFragment = new HistoryFragment();
                        transaction.replace(R.id.fragment_container, historyFragment);
                        transaction.commit();
                        break;

                    case R.id.switch_analysis:
                        if (null == analysisFragment) {
                            analysisFragment = new AnalysisFragment();
                        }
                        transaction = getFragmentManager().beginTransaction();
                        transaction.remove(analysisFragment);
                        analysisFragment = new AnalysisFragment();
                        transaction.replace(R.id.fragment_container, analysisFragment);
                        transaction.commit();
                        break;
                }
            }
        });
        return view;
    }

    public void initData() {
        transaction = getFragmentManager().beginTransaction();
        if (null == budgetFragment) {
            budgetFragment = new BudgetFragment();
        }
        transaction.add(R.id.linearLayout_rg, budgetFragment);
        transaction.commit();
    }

    public static Fragment newInstance(int position) {
        Fragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        args.putInt("selection", position);
        fragment.setArguments(args);
        return fragment;
    }
}

