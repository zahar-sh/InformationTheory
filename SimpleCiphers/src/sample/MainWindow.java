package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import sample.logic.*;

import java.io.*;

public class MainWindow {
    @FXML
    private TextArea encodeTF;
    @FXML
    private TextField encodeKey;
    @FXML
    private ChoiceBox<Cipher> encodeCB;

    @FXML
    private TextArea decodeTF;
    @FXML
    private TextField decodeKey;
    @FXML
    private ChoiceBox<Cipher> decodeCB;
    @FXML
    private TextArea result;

    @FXML
    private TextField fEncodeIn, fEncodeOut, fEncodeKey;
    @FXML
    private ChoiceBox<Cipher> fEncodeCB;

    @FXML
    private TextField fDecodeIn, fDecodeOut, fDecodeKey;
    @FXML
    private ChoiceBox<Cipher> fDecodeCB;

    @FXML
    private Label out;

    public static final String EN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String RU = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static String make(String in, String pattern) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (pattern.indexOf(c) >= 0)
                sb.append(c);
        }
        return sb.toString();
    }

    private final ObservableList<Cipher> ciphers = FXCollections.observableArrayList(
            new Filter(new Cipher1()) {
                public String apply(String s) {
                    return make(s.toUpperCase(), RU);
                }
            },
            new Filter(new Cipher2(RU)) {
                public String apply(String s) {
                    return make(s.toUpperCase(), RU);
                }
            },
            new Filter(new Cipher3(5, EN.replace("J", ""), 'X') {
                protected char chPosApply(char c) {
                    return c == 'J' ? 'I' : c;
                }
            }) {
                public String apply(String s) {
                    return make(s.toUpperCase(), EN);
                }
            }
    );
    private final String[] values = {
            "Column Cipher",
            "Vigener Cipher",
            "Pleifer Cipher"
    };

    private final FileChooser chooser = new FileChooser();

    @FXML
    private void initialize() {
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt"));
        StringConverter<Cipher> converter = new StringConverter<>() {
            public String toString(Cipher cipher) {
                for (int i = 0; i < ciphers.size(); i++)
                    if (cipher == ciphers.get(i))
                        return values[i];
                return String.valueOf(cipher);
            }

            public Cipher fromString(String s) {
                if (s != null)
                    for (int i = 0; i < ciphers.size(); i++)
                        if (s.equals(values[i]))
                            return ciphers.get(i);
                return null;
            }
        };

        ChoiceBox<Cipher>[] boxes = new ChoiceBox[]{
                encodeCB, decodeCB, fEncodeCB, fDecodeCB
        };
        for (ChoiceBox<Cipher> box : boxes) {
            box.setConverter(converter);
            box.setItems(ciphers);
            box.setValue(ciphers.get(0));
        }
    }

    private String onlyTxtFile(String path) throws FileNotFoundException {
        if (path == null || path.length() < 4 || !path.endsWith(".txt"))
            throw new FileNotFoundException("Is not txt file");
        return path;
    }
    private void logErr(Exception e, String msg) {
        out.setText((e == null || e.getMessage() == null) ? msg : (msg + ": " + e.getMessage()));
    }

    private void loadTF(TextArea area) {
        File in = chooser.showOpenDialog(null);
        if (in != null) {
            try {
                area.setText(Files.readAll(in));
            } catch (Exception e) {
                logErr(e, "IO exception");
            }
        }
    }
    private void saveTF(TextArea area) {
        File out = chooser.showSaveDialog(null);
        if (out != null) {
            try {
                Files.writeAll(out, area.getText());
            } catch (Exception e) {
                logErr(e, "IO exception");
            }
        }
    }

    //Console encode
    @FXML
    private void loadToEncodeTF() {
        loadTF(encodeTF);
    }
    @FXML
    private void saveFromEncodeTF() {
        saveTF(encodeTF);
    }
    @FXML
    private void encode() {
        Cipher cipher = encodeCB.getSelectionModel().getSelectedItem();
        try {
            String encode = cipher.encode(encodeTF.getText(), encodeKey.getText());
            decodeTF.setText(encode);
        } catch (Exception e) {
            logErr(e, "Encode exception");
        }
    }

    //Console decode
    @FXML
    private void loadToDecodeTF() {
        loadTF(decodeTF);
    }
    @FXML
    private void saveFromDecodeTF() {
        saveTF(decodeTF);
    }

    @FXML
    private void decode() {
        Cipher cipher = decodeCB.getSelectionModel().getSelectedItem();
        try {
            String decode = cipher.decode(decodeTF.getText(), decodeKey.getText());
            result.setText(decode);
        } catch (Exception e) {
            logErr(e, "Decode exception");
        }
    }
    @FXML
    private void saveResult() {
        saveTF(result);
    }

    //File encode
    private void open(TextField field) {
        File file = chooser.showOpenDialog(null);
        if (file != null)
            field.setText(file.getPath());
    }
    private void save(TextField field) {
        File file = chooser.showSaveDialog(null);
        if (file != null)
            field.setText(file.getPath());
    }

    @FXML
    private void selectFEncodeIn() {
        open(fEncodeIn);
    }
    @FXML
    private void selectFEncodeOut() {
        save(fEncodeOut);
    }
    @FXML
    private void fEncode() {
        Cipher cipher = fEncodeCB.getSelectionModel().getSelectedItem();
        try {
            FileInputStream in = new FileInputStream(onlyTxtFile(fEncodeIn.getText()));
            FileOutputStream out = new FileOutputStream(onlyTxtFile(fEncodeOut.getText()));
            String encode = cipher.encode(Files.readAll(in), fEncodeKey.getText());
            Files.writeAll(out, encode);
        } catch (FileNotFoundException e) {
            logErr(e, "File not found");
        } catch (IOException e) {
            logErr(e, "IO exception");
        } catch (Exception e) {
            logErr(e, "Encode exception");
        }
    }

    //File decode
    @FXML
    private void selectFDecodeIn() {
        open(fDecodeIn);
    }
    @FXML
    private void selectFDecodeOut() {
        save(fDecodeOut);
    }
    @FXML
    private void fDecode() {
        Cipher cipher = fDecodeCB.getSelectionModel().getSelectedItem();
        try {
            FileInputStream in = new FileInputStream(onlyTxtFile(fDecodeIn.getText()));
            FileOutputStream out = new FileOutputStream(onlyTxtFile(fDecodeOut.getText()));
            String decode = cipher.decode(Files.readAll(in), fDecodeKey.getText());
            Files.writeAll(out, decode);
        } catch (FileNotFoundException e) {
            logErr(e, "File not found");
        } catch (IOException e) {
            logErr(e, "IO exception");
        } catch (Exception e) {
            logErr(e, "Decode exception");
        }
    }

    @FXML
    private void clearOut() {
        out.setText("");
    }
}