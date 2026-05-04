package panelDrawNumber.actionButtons;

import panelDrawNumber.DrawingPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SelectNumber implements ActionListener {
    DrawingPanel dp;

    public SelectNumber(DrawingPanel dp) {
        this.dp = dp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (BufferedReader br = new BufferedReader(new FileReader("out/artifacts/AI_jar/mnist_train_valid.csv"))) {
            br.readLine();
            for (int i = 0; i < dp.index; i++) {
                br.readLine();
            }
            String line = br.readLine();
            String[] values = line.split(";");
            double[] valuesDouble = new double[784];
            int[][] records = new int[dp.drawingBoard.getPositions().length][dp.drawingBoard.getPositions()[0].length];

            for (int i = 0; i < valuesDouble.length; i++) {
                valuesDouble[i] = Integer.parseInt(values[i + 2]);
            }

            for (int i = 0; i < records.length; i++) {
                for (int j = 0; j < records[i].length; j++) {
                    records[j][i] = (int) valuesDouble[i * records.length + j];
                }
            }
            dp.expectedAnswer = Integer.parseInt(values[0]);
            dp.drawingBoard.setPositions(records);
            dp.index++;
            System.out.println(dp.index);
            dp.setAnswer(false);
        } catch (IOException ex) {
            System.out.println("No se ha podido encontrar los casos de prueba. Por favor, verifique que el archivo está ahí y que es el correcto");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}