package ley.jmclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import ley.jensmemes.HTTPClient;
import ley.jensmemes.model.Category;
import ley.jensmemes.model.response.CategoriesResponse;
import ley.jensmemes.model.response.UploadResponse;

import java.io.*;
import java.net.URISyntaxException;

public class Controller {
    public ChoiceBox<Category> boxCat;
    public Label lblCat;
    public Label lblTok;
    public TextField txTok;
    public Button btnSave;
    public Button btnReload;
    public Label lblFile;
    public Button btnFile;
    public Label lblSel;
    public Button btnUpload;

    File selection = null;
    String token;
    String category;
    HostServices serv;

    public Controller() {
        reload();
        loadToken();
    }

    public void setHostServices(HostServices serv) {
        this.serv = serv;
    }

    public void reload() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxCat.setVisible(false);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CategoriesResponse res = HTTPClient.categories();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            boxCat.getItems().clear();
                            for (Category c : res.categories) {
                                boxCat.getItems().add(c);
                            }
                            boxCat.setVisible(true);
                        }
                    });
                } catch (IOException | URISyntaxException e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            showError(e.getLocalizedMessage());
                        }
                    });
                }
            }
        }).start();
    }

    public void chooseFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Meme");
        Node source = (Node) event.getSource();
        selection = fc.showOpenDialog(source.getScene().getWindow());
        lblSel.setText(selection.getAbsolutePath());
    }

    public void upload(ActionEvent event) {
        if (selection != null) {
            if (boxCat.getValue() != null) {
                category = boxCat.getValue().id;
                token = txTok.getText();

                btnUpload.setDisable(true);
                btnUpload.setVisible(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UploadResponse res = HTTPClient.upload(token, category, selection);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    btnUpload.setDisable(false);
                                    btnUpload.setVisible(true);
                                    if (res.status != 201) {
                                        showError(res.error);
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Upload Complete");
                                        alert.setHeaderText("Meme uploaded");
                                        alert.setContentText(res.files.get(0));
                                        alert.showAndWait();
                                        if (serv != null) {
                                            serv.showDocument(res.files.get(0));
                                        }
                                    }
                                }
                            });
                        } catch (IOException | URISyntaxException e) {
                            showError(e.getLocalizedMessage());
                        }
                    }
                }).start();
            } else {
                showError("No category selected");
            }
        } else {
            showError("No file selected to upload");
        }
    }

    public void saveToken() {
        File tokfile = new File(System.getProperty("user.home") + "/.config/jensmemes/token");
        token = txTok.getText();
        new Thread(new Runnable() {
            @Override
            public void run() {
                tokfile.getParentFile().mkdirs();
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tokfile));
                    writer.write(token);
                    writer.close();
                } catch (IOException e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            showError(e.getLocalizedMessage());
                        }
                    });
                }
            }
        }).start();
    }

    public void loadToken()  {
        File tokfile = new File(System.getProperty("user.home") + "/.config/jensmemes/token");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (tokfile.exists()) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(tokfile));
                        token = br.readLine();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                txTok.setText(token);
                            }
                        });
                    } catch (IOException e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                showError(e.getLocalizedMessage());
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error:");
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
