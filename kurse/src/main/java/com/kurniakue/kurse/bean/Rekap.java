/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.kurse.bean;

import com.kurniakue.kurse.Replier;
import com.kurniakue.data.DateInfo;
import com.kurniakue.data.Transaction;
import com.kurniakue.kurse.KurseContext;
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
        Replier.get().add("Rekapitulate transaction in " + dateInfo.getString(DateInfo.F.ThisYearMonth)).send();
        new Transaction().recapitulate(dateInfo);
        Replier.get().add("Rekapitulation complete.");
    }

    private KurseContext getContext() {
        return KurseContext.getContext();
    }
}
