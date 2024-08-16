package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import logic.BitGenerator;
import logic.Encoder;

import java.io.File;

public class Controller {
    @FXML
    private TextField in, out, key, inBits, keyBits, outBits;

    @FXML
    private TextArea inBits0, keyBits0, outBits0;

    @FXML
    private Label label;

    private final FileChooser fileChooser = new FileChooser();

    private static int getKey(TextField encodeKey) {
        final String s = filterKey(encodeKey.getText().trim());
        final int l = s.length();
        if (l == 32) {
            int bits = 0;
            for (int i = 0; i < l; i++) {
                bits <<= 1;
                if (s.charAt(i) == '1')
                    bits |= 1;
            }
            return bits;
        }
        throw new IllegalArgumentException("Key length is not 32: " + s.length());
    }
    private static String filterKey(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        char tmp;
        for (int i = 0; i < s.length(); i++) {
            tmp = s.charAt(i);
            if (tmp == '0' || tmp == '1')
                sb.append(tmp);
        }
        return sb.toString();
    }

    private static File fileFromTF(TextField f) {
        return new File(f.getText().trim());
    }

    private void setIn(TextField in) {
        File f = fileChooser.showOpenDialog(null);
        in.setText(f == null ? "" : f.getAbsolutePath());
    }
    private void setOut(TextField out) {
        File f = fileChooser.showSaveDialog(null);
        out.setText(f == null ? "" : f.getAbsolutePath());
    }

    private void log(Exception e) {
        label.setText((e == null || e.getMessage() == null) ? "Encode exception" :
                ("Encode exception: " + e.getMessage()));
    }

    @FXML
    private void selectEncodeIn() {
        setIn(in);
    }

    @FXML
    private void selectEncodeOut() {
        setOut(out);
    }

    @FXML
    private void selectEncodeInBits() {
        setOut(inBits);
    }

    @FXML
    private void selectEncodeKeyBits() {
        setOut(keyBits);
    }

    @FXML
    private void selectEncodeOutBits() {
        setOut(outBits);
    }

    @FXML
    private void encode() {
        try {
            int key = getKey(this.key);
            long start = System.currentTimeMillis();
            Encoder.fileEncode(fileFromTF(in), fileFromTF(out), new BitGenerator(key));
            label.setText("Encoded at: " + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            log(e);
        }
    }

    @FXML
    private void detailEncode() {
        try {
            int key = getKey(this.key);
            File ib = fileFromTF(inBits);
            File kb = fileFromTF(keyBits);
            File ob = fileFromTF(outBits);

            int limit = 8192;
            long start = System.currentTimeMillis();
            //Encoder.fileEncode(fileFromTF(in), fileFromTF(out),  new BitGenerator(key), ib, kb, ob, limit);
            Encoder.fileEncode(fileFromTF(in), fileFromTF(out),  new BitGenerator(key), ib, kb, ob);
            label.setText("Encoded at: " + (System.currentTimeMillis() - start) + "ms");

            /*inBits0.setText(Encoder.readAll(ib));
            keyBits0.setText(Encoder.readAll(kb));
            outBits0.setText(Encoder.readAll(ob));*/

            inBits0.setText(Encoder.readNBytes(ib, limit));
            keyBits0.setText(Encoder.readNBytes(kb, limit));
            outBits0.setText(Encoder.readNBytes(ob, limit));
        } catch (Exception e) {
            log(e);
        }
    }

    @FXML
    private void clearOut() {
        inBits0.setText("");
        keyBits0.setText("");
        outBits0.setText("");
        label.setText("");
    }
}