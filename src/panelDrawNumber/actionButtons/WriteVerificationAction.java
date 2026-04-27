package panelDrawNumber.actionButtons;

import panelDrawNumber.DrawingPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class WriteVerificationAction implements ActionListener {
    DrawingPanel dp;
    String type;

    public WriteVerificationAction(DrawingPanel dp, String type) {
        this.dp = dp;
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long filaN = 3;
        String fileName = "mnist_train_valid.csv";
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            for (int i = 0; i < dp.index; i++) {
                String name = br.readLine();
                filaN += name.length() + 2;
            }
            raf.seek(filaN + 1);
            raf.writeBytes(type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
