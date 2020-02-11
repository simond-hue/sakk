package com.company.felulet;

import com.company.logika.SakkTabla;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SakkFelulet extends JFrame {

    private SakkTabla tabla;

    private Container foAblak;
    private JPanel pnlJatekTabla;

    private Mezo kezdoMezo, erkezesiMezo;

    private JPanel pnlallapot;

    private JLabel fekete;
    private JLabel feher;

    private JLabel aktualisjatekos;
    private JLabel fehervagyfekete;

    private JPanel pnloldalso;

    private boolean lepes = true;

    private long stopperInditasa;
    private JLabel lblStopper;
    private Timer tmr;

    public SakkFelulet(){
        this.tabla = new SakkTabla();
        this.initComponents();

        this.sakkTablaMegjelenit();
    }

    private void initComponents(){

        this.setTitle("Sakk 1.0");

//        this.setUndecorated(true);
//        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setSize(width,height);

        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);

        this.foAblak = this.getContentPane();
        this.foAblak.setLayout(new BorderLayout(10,10));

        this.pnlJatekTabla = new JPanel();
        this.pnlJatekTabla.setLayout(new GridLayout(9,9));

        this.foAblak.add(BorderLayout.CENTER, this.pnlJatekTabla);

        this.pnlallapot = new JPanel();
        this.pnlallapot.setLayout(new GridLayout(1,3));
        this.foAblak.add(BorderLayout.SOUTH, this.pnlallapot);

        this.pnloldalso = new JPanel();
        this.pnloldalso.setLayout(new GridLayout(3,1));
        this.foAblak.add(BorderLayout.EAST, this.pnloldalso);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void sakkTablaMegjelenit(){
        this.pnlJatekTabla.removeAll();

        this.pnlJatekTabla.add(new JLabel());

        for (int i = 0; i < 8; i++) {
            JLabel hivatkozasABC = new JLabel();
            hivatkozasABC.setText(String.format("%s.", (char)(i+65)));
            hivatkozasABC.setHorizontalAlignment((SwingConstants.CENTER));
            hivatkozasABC.setVerticalAlignment((SwingConstants.CENTER));
            this.pnlJatekTabla.add(hivatkozasABC);
        }

        for (int i = 0; i < 8; i++) {
            JLabel hivatkozas123 = new JLabel();
            hivatkozas123.setText(String.format("%d.", (8-i)));
            hivatkozas123.setHorizontalAlignment((SwingConstants.CENTER));
            hivatkozas123.setVerticalAlignment((SwingConstants.CENTER));
            this.pnlJatekTabla.add(hivatkozas123);

            for (int j = 0; j < 8; j++) {
                Mezo m = new Mezo(i,j, tabla.getErtek(i,j));

                m.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mezoKattintas(e);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover(e);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        cursorexit(e);
                    }
                });


                this.pnlJatekTabla.add(m);
            }
        }

        JLabel allapot = new JLabel();
        allapot.setText("Állapot");
        allapot.setHorizontalAlignment((SwingConstants.CENTER));
        allapot.setVerticalAlignment((SwingConstants.CENTER));

        this.pnlallapot.add(allapot);

        feher = new JLabel(String.format("Fehér: %d",tabla.getVilagosFigurakSzama()));
        fekete = new JLabel(String.format("Fekete: %d",tabla.getSotetFigurakSzama()));

        aktualisjatekos = new JLabel(String.format("Aktuális játékos"));
        fehervagyfekete = new JLabel(String.format("%s",lepes?"Fehér":"Fekete"));

        this.pnloldalso.add(aktualisjatekos);
        this.pnloldalso.add(fehervagyfekete);

        this.stopperInditasa = new Date().getTime();
        this.lblStopper = new JLabel();
        TimerTick();
        this.pnloldalso.add(lblStopper);

        this.pnlallapot.add(feher);
        this.pnlallapot.add(fekete);

        this.revalidate();
        this.repaint();
    }


    private void mezoKattintas(MouseEvent me){
        Mezo aktualisMezo = (Mezo)me.getSource();

        if (kezdoMezo == null && aktualisMezo.getErtek() != 0){
            kezdoMezo = aktualisMezo;
            kezdoMezo.setBackground(Color.gray);
        } else if (kezdoMezo != null && erkezesiMezo == null && aktualisMezo != kezdoMezo){
            erkezesiMezo = aktualisMezo;
        }

        if (kezdoMezo != null && erkezesiMezo != null){
            int sx = kezdoMezo.getPozicioX();
            int sy = kezdoMezo.getPozicioY();

            int dx = erkezesiMezo.getPozicioX();
            int dy = erkezesiMezo.getPozicioY();

            if( (tabla.isVilagosFigura(sx,sy) && lepes && tabla.isErvenyesGyalogLepes(sx,sy,dx,dy)) ||
                (tabla.isSotetFigura(sx,sy) && !lepes)){
                this.tabla.lep(sx,sy,dx,dy);
                lepes = !lepes;
            }

            feher.setText(String.format("Fehér: %d",tabla.getVilagosFigurakSzama()));
            fekete.setText(String.format("Fekete: %d",tabla.getSotetFigurakSzama()));
            fehervagyfekete.setText(String.format("%s",lepes?"Fehér":"Fekete"));
            this.kezdoMezo.setErtek(this.tabla.getErtek(sx,sy));
            this.erkezesiMezo.setErtek(this.tabla.getErtek(dx,dy));

            this.kezdoMezo = null;
            this.erkezesiMezo = null;
        }
    }

    private void hover(MouseEvent e){
        Mezo m = (Mezo)e.getSource();
        if(kezdoMezo != m) m.setBackground(Color.lightGray);
    }

    private void cursorexit(MouseEvent e){
        Mezo m = (Mezo)e.getSource();
        if(kezdoMezo != m) m.frissit();
    }

    public void TimerTick(){
        this.tmr = new Timer();
        this.tmr.schedule(new TimerTask() {

            @Override
            public void run() {
                lblStopper.setText(String.format("%02d:%02d",
                        (new Date().getTime() - stopperInditasa)/1000/60,
                        ((new Date().getTime() - stopperInditasa)/1000)%60));
            }
        },0,100);
    }
}


