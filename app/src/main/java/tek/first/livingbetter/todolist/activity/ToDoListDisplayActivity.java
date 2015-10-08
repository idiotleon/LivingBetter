package tek.first.livingbetter.todolist.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import tek.first.livingbetter.NavigationDrawerFragment;
import tek.first.livingbetter.R;
import tek.first.livingbetter.habit.HabitDisplayFragment;
import tek.first.livingbetter.provider.DatabaseHelper;
import tek.first.livingbetter.todolist.fragment.CompletedDetailedItemsDisplayFragment;
import tek.first.livingbetter.todolist.fragment.CompletedSimpleToDoItemsDisplayFragment;
import tek.first.livingbetter.todolist.fragment.IncompleteDetailedItemsDisplayFragment;
import tek.first.livingbetter.todolist.fragment.IncompleteSimpleToDoItemsDisplayFragment;
import tek.first.livingbetter.todolist.fragment.NewItemAddedFragment;
import tek.first.livingbetter.todolist.fragment.adapter.CompletedDetailedToDoItemsListViewCustomAdapter;
import tek.first.livingbetter.todolist.fragment.adapter.CompletedSimpleToDoItemsListViewCustomAdatper;
import tek.first.livingbetter.todolist.fragment.adapter.SimpleToDoItemsListViewCustomAdapter;
import tek.first.livingbetter.todolist.fragment.adapter.ToDoItemsListViewCustomAdapter;
import tek.first.livingbetter.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import tek.first.livingbetter.todolist.helper.GeneralConstants;
import tek.first.livingbetter.todolist.helper.GeneralHelper;
import tek.first.livingbetter.todolist.model.Date;
import tek.first.livingbetter.todolist.model.DetailedToDoItem;
import tek.first.livingbetter.todolist.model.SimpleToDoItem;
import tek.first.livingbetter.todolist.model.Time;
import tek.first.livingbetter.wallet.WalletFragment;

public class ToDoListDisplayActivity extends AppCompatActivity
        implements DetailedNewToDoItemDialogFragment.OnNewItemAddedListener,
        NewItemAddedFragment.OnNewSimpleItemAddedListener,
        GeneralHelper.ToDoItemStatusChangeListener {

    public static String LOG_TAG = ToDoListDisplayActivity.class.getSimpleName();

    private Date dateSelected = null;
    private Time timeSelected = null;

    private ArrayList<DetailedToDoItem> incompleteDetailedToDoItemsArrayList;
    private ArrayList<SimpleToDoItem> incompleteSimpleToDoItemsArrayList;
    private ArrayList<DetailedToDoItem> completedDetailedToDoItemsArrayList;
    private ArrayList<SimpleToDoItem> completedSimpleToDoItemsArrayList;

    private ToDoItemsListViewCustomAdapter incompleteToDoItemsCustomAdapter;
    private CompletedDetailedToDoItemsListViewCustomAdapter completedDetailedToDoItemsCustomAdapter;
    private CompletedSimpleToDoItemsListViewCustomAdatper completedSimpleToDoItemsListViewCustomAdatper;
    private SimpleToDoItemsListViewCustomAdapter simpleToDoItemsListViewCustomAdapter;

    private NewItemAddedFragment newItemAddedFragment;
    private IncompleteDetailedItemsDisplayFragment incompleteToDoItemDisplayListFragment;
    private CompletedDetailedItemsDisplayFragment completedDetailedToDoItemDisplayListFragment;
    private CompletedSimpleToDoItemsDisplayFragment completedSimpleToDoItemDisplayListFragment;
    private IncompleteSimpleToDoItemsDisplayFragment simpleToDoItemsDisplayFragment;

    private static int counterOfSortByPrioritySelectedTimes = 0;
    private static int counterOfSortByDeadlineSelectedTimes = 0;
    private static int counterOfSortByTimeAddedSelectedTimes = 0;
    private static int counterOfSortByTitleSelectedTimes = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_main_activity);

        incompleteDetailedToDoItemsArrayList = new ArrayList<>();
        completedDetailedToDoItemsArrayList = new ArrayList<>();
        incompleteSimpleToDoItemsArrayList = new ArrayList<>();
        completedSimpleToDoItemsArrayList = new ArrayList<>();

        FragmentManager fragmentManager = getFragmentManager();
        newItemAddedFragment
                = (NewItemAddedFragment) fragmentManager.findFragmentById(R.id.todolist_newitem);
        incompleteToDoItemDisplayListFragment
                = (IncompleteDetailedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_incomplete_items);
        completedDetailedToDoItemDisplayListFragment
                = (CompletedDetailedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_completed_items);
        completedSimpleToDoItemDisplayListFragment
                = (CompletedSimpleToDoItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_completed_simple_items);
        simpleToDoItemsDisplayFragment
                = (IncompleteSimpleToDoItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_simple_todoitems);

        refreshPage(savedInstanceState);

/*        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

            ArrayList<CharSequence> a1 = new ArrayList<>();
            a1.add("Item1");
            a1.add("Item2");
            ArrayAdapter<CharSequence> dropDownAdapter =
                    new ArrayAdapter<CharSequence>(this, android.R.layout.simple_expandable_list_item_1, a1);
            actionBar.setListNavigationCallbacks(dropDownAdapter, new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                    Log.v(LOG_TAG, "onNavigationItemSelected, ToDoListDisplayActivity: " + itemId);
                    return true;
                }
            });*/

/*             actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setDisplayShowTitleEnabled(false);
           ActionBar.Tab incompleteToDoItemsTab = actionBar.newTab();
            incompleteToDoItemsTab.setText("Incomplete ToDoItems").setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }
            });

            ActionBar.Tab completeToDoItemsTab = actionBar.newTab().setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }
            });
            completeToDoItemsTab.setText("Complete ToDoItems");

            ActionBar.Tab simpleToDoItemsTab = actionBar.newTab().setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }
            });
            simpleToDoItemsTab.setText("Simple ToDoItems");

            actionBar.addTab(incompleteToDoItemsTab);
            actionBar.addTab(completeToDoItemsTab);
            actionBar.addTab(simpleToDoItemsTab);
        } else {
            Log.e(LOG_TAG, "actionBar is null, ToDoListDisplayActivity");
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todolist_display_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.display_menu_activitytodolist:
                Log.v(LOG_TAG, "Displaying-Menu Selected");
                AlertDialog.Builder sortBuilder = new AlertDialog.Builder(ToDoListDisplayActivity.this);
                sortBuilder.setTitle(getResources().getString(R.string.please_select_what_to_be_displayed))
                        .setItems(R.array.menu_display_standard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:     // Display only incomplete items, sorted in SharedPreference mode
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, incompleteToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, completedDetailedToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, simpleToDoItemsDisplayFragment);
                                        break;
                                    case 1:     // Display only completed items
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, completedDetailedToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, incompleteToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, simpleToDoItemsDisplayFragment);
                                        break;
                                    case 2: // Display all simple todoitems
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, simpleToDoItemsDisplayFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, completedDetailedToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, incompleteToDoItemDisplayListFragment);
                                        break;
                                    default:     // Display all items
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, completedDetailedToDoItemDisplayListFragment);
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, incompleteToDoItemDisplayListFragment);
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, simpleToDoItemsDisplayFragment);
                                        break;
                                }
                            }
                        });
                (sortBuilder.create()).show();
                break;
            case R.id.sortby_menu_activitytodolist:
                Log.v(LOG_TAG, "Sorting-Menu Selected");
                AlertDialog.Builder displayBuilder = new AlertDialog.Builder(ToDoListDisplayActivity.this);
                displayBuilder.setTitle(getResources().getString(R.string.please_select_a_sorting_standard))
                        .setItems(R.array.menu_sort_standard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:     // Sort by priority
                                        setSortingPreference(DatabaseHelper.SORT_BY_PRIORITY, counterOfSortByPrioritySelectedTimes);
                                        counterOfSortByPrioritySelectedTimes++;
                                        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
                                        break;
                                    case 1:     // Sort by deadline
                                        setSortingPreference(DatabaseHelper.SORT_BY_DEADLINE, counterOfSortByDeadlineSelectedTimes);
                                        counterOfSortByDeadlineSelectedTimes++;
                                        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
                                        break;
                                    case 2:     // Sort by time added
                                        setSortingPreference(DatabaseHelper.SORT_BY_TIME_ADDED, counterOfSortByTimeAddedSelectedTimes);
                                        counterOfSortByTimeAddedSelectedTimes++;
                                        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
                                        break;
                                    case 3:     // Sort by title
                                        setSortingPreference(DatabaseHelper.SORT_BY_TITLE, counterOfSortByTitleSelectedTimes);
                                        counterOfSortByTitleSelectedTimes++;
                                        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
                                        break;
                                }
                            }
                        });
                (displayBuilder.create()).show();
                break;
/*            case R.id.preference_menu_activitytodolist:
                Intent intent = new Intent(this, ToDoListPreferenceActivity.class);
                startActivityForResult(intent, GeneralConstants.SHOW_PREFERENCES);*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSortingPreference(String sortingPreference, int counter) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ToDoListDisplayActivity.this);

        sharedPreferences.edit().putString(GeneralConstants.TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER, sortingPreference).commit();

        if (counter % 2 == 0) {
            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_DESC).commit();
            Log.v(LOG_TAG, "Set sorting sharedPreference DESC");
        } else {
            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_ASC).commit();
            Log.v(LOG_TAG, "Set sorting sharedPreference ASC");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_DETAILED_TODOITEMS_ARRAYLIST_IDENTIFIER, incompleteDetailedToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_SIMPLE_TODOITEM_IDENTIFIER, incompleteSimpleToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_COMPLETED_DETAILED_TODOITEMS_ARRAYLIST_IDENTIFIER, completedDetailedToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_COMPLETED_SIMPLE_TODOITEMS_ARRAYLIST_IDENTIFIER, completedSimpleToDoItemsArrayList);
    }

    @Override
    public void onNewItemAdded(DetailedToDoItem newDetailedToDoItem) {
        Toast.makeText(ToDoListDisplayActivity.this, "A new DetailedToDoItem added", Toast.LENGTH_SHORT).show();
        GeneralHelper.insertToDoListItem(ToDoListDisplayActivity.this, newDetailedToDoItem);
        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
    }

    @Override
    public void onNewSimpleItemAdded(SimpleToDoItem newSimpleToDoItem) {
        GeneralHelper.insertToDoListItem(ToDoListDisplayActivity.this, newSimpleToDoItem);
        Log.v(LOG_TAG, "A new simple ToDoItem added.");
        Toast.makeText(ToDoListDisplayActivity.this, "A new simple ToDoItem added", Toast.LENGTH_SHORT).show();
        incompleteSimpleToDoItemsArrayList.add(0, newSimpleToDoItem);
        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
    }

    @Override
    public void onStatusChanged() {
        Log.v(LOG_TAG, "onStatusChanged(), ToDoListDisplayActivity executed.");
        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
    }

    private void refreshPage(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            incompleteDetailedToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_DETAILED_TODOITEMS_ARRAYLIST_IDENTIFIER);
            completedDetailedToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_COMPLETED_DETAILED_TODOITEMS_ARRAYLIST_IDENTIFIER);
            incompleteSimpleToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_SIMPLE_TODOITEM_IDENTIFIER);
            completedSimpleToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_COMPLETED_SIMPLE_TODOITEMS_ARRAYLIST_IDENTIFIER);
        } else {
            incompleteDetailedToDoItemsArrayList = GeneralHelper.getSortedIncompleteDetailedToDoItemsAsArrayList(ToDoListDisplayActivity.this);
            completedDetailedToDoItemsArrayList = GeneralHelper.getSortedCompletedDetailedToDoItemsAsArrayList(ToDoListDisplayActivity.this);
            incompleteSimpleToDoItemsArrayList = GeneralHelper.getSortedIncompleteSimpleToDoItemsAsArrayList(ToDoListDisplayActivity.this);
            completedSimpleToDoItemsArrayList = GeneralHelper.getSortedCompletedSimpleToDoItemsAsArrayList(ToDoListDisplayActivity.this);
        }

        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListDisplayActivity.this, incompleteDetailedToDoItemsArrayList);
        if (!incompleteDetailedToDoItemsArrayList.isEmpty()) {
            incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
        } else {
            incompleteToDoItemDisplayListFragment.setListAdapter(null);
        }

        completedDetailedToDoItemsCustomAdapter = new CompletedDetailedToDoItemsListViewCustomAdapter(ToDoListDisplayActivity.this, completedDetailedToDoItemsArrayList);
        if (!completedDetailedToDoItemsArrayList.isEmpty()) {
            completedDetailedToDoItemDisplayListFragment.setListAdapter(completedDetailedToDoItemsCustomAdapter);
        } else {
            completedDetailedToDoItemDisplayListFragment.setListAdapter(null);
        }

        completedSimpleToDoItemsListViewCustomAdatper = new CompletedSimpleToDoItemsListViewCustomAdatper(ToDoListDisplayActivity.this, completedSimpleToDoItemsArrayList);
        if (!completedSimpleToDoItemsArrayList.isEmpty()) {
            completedSimpleToDoItemDisplayListFragment.setListAdapter(completedSimpleToDoItemsListViewCustomAdatper);
        } else {
            completedSimpleToDoItemDisplayListFragment.setListAdapter(null);
        }

        simpleToDoItemsListViewCustomAdapter = new SimpleToDoItemsListViewCustomAdapter(ToDoListDisplayActivity.this, incompleteSimpleToDoItemsArrayList);
        if (!incompleteSimpleToDoItemsArrayList.isEmpty()) {
            simpleToDoItemsDisplayFragment.setListAdapter(simpleToDoItemsListViewCustomAdapter);
        } else {
            simpleToDoItemsDisplayFragment.setListAdapter(null);
        }
    }
}
