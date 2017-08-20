/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.kurse.bean;

import com.kurniakue.kurse.Replier;
import com.kurniakue.data.DateInfo;
import com.kurniakue.data.Transaction;
import com.kurniakue.kurse.ReqContext;
import java.util.Calendar;

/**
 *
 * @author Harun Al Rasyid
 */
public class Rekap {
    public void calculate()
    {
        System.out.println("Rekap.calculate");
        Calendar calendar = getContext().getCurrentCalendar();
        DateInfo dateInfo = DateInfo.getDateInfo(calendar);
        Replier.get().add("Recapitulate transaction in " + dateInfo.getString(DateInfo.F.ThisYearMonth))
                .add("\n");
        new Transaction().recapitulate(dateInfo);
        Replier.get().add("Recapitulation complete.").send();
    }

    private ReqContext getContext() {
        return ReqContext.get();
    }
}
