package com.strategy.intecom.vtc.fixrepairer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.view.custom.calendarview.CalendarListener;
import com.strategy.intecom.vtc.fixrepairer.view.custom.calendarview.CustomCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mr. Ha on 5/20/16.
 */
public class DlCalendar extends Dialog {

    private CustomCalendarView calendarView;
    private Context context;

    private int width;

    public DlCalendar(Context context, int width) {
        super(context);
        this.context = context;
        this.width = (int) (width - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = width;
        getWindow().setAttributes(lp);
        setContentView(R.layout.dl_calendar);

        initControler();
    }

    public void initControler() {
        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Setting custom font
//        final Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Arch_Rival_Bold.ttf");
//        if (null != typeface) {
//            calendarView.setCustomTypeface(typeface);
//            calendarView.refreshCalendar(currentCalendar);
//        }

        //Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Toast.makeText(context, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                Toast.makeText(context, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
