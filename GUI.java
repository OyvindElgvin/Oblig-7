import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.event.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Scanner;
import javafx.geometry.HPos;


public class GUI extends Application {
	private int antallRader;
	private int antallKolonner;
	private Text infoPaaSkjerm;
	private Rute brett[];
	private boolean flereUtveier = true;
	private Labyrint labyrint = null;
	private GridPane rutenett = null;
	private Liste<String> losningString = null;
	private Pane kulisser;
	private Button nesteUtvei;

	// RUTE
	class Rute extends Button {
		public char merke = ' ';

		Rute() {
			super(" ");
			// setter størrelsen på rutene slik at størrelsen passer for alle labyrintfilene
			double x = 500/(antallRader);
			double y = 300/(antallKolonner);
			setFont(new Font((x+y)/2.3));
			setPrefSize(x, y);
		}

		void settMerke(char c) {
			setText(""+c);
			merke = c;
		}
	}

	// 			---------- EventHandlere ----------

	// viser en første utvei
	class KlikkRuteBehandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			boolean[][] enUtvei;
			try {
				if (flereUtveier) {

					// henter indexen til den klikkede ruta
					int x = rutenett.getColumnIndex((Rute)e.getSource());
					int y = rutenett.getRowIndex((Rute)e.getSource());

					// finner utveiene
					try {
						losningString = labyrint.finnUtveiFra(x, y);
					} catch (Exception ex) {}

					// hvis svart rute
					Rute nyRute = (Rute)e.getSource();
					if (nyRute.merke == '#') {
						kulisser.getChildren().remove(infoPaaSkjerm);
						infoPaaSkjerm.setText("Velg en hvit rute!");
						kulisser.getChildren().add(infoPaaSkjerm);
					}
					// eller hvis ingen utveier
					else if (losningString.stoerrelse() == 0) {
						kulisser.getChildren().remove(infoPaaSkjerm);
						infoPaaSkjerm.setText("Ingen utveier");
						kulisser.getChildren().add(infoPaaSkjerm);
					}

					// henter en utvei fra lista og viser den
					String liste = losningString.hent(0);
					losningString.fjern(0);
					enUtvei = losningStringTilTabell(liste, antallKolonner, antallRader);
					for (int i = 0; i < antallRader; i++) {
						for (int j = 0; j < antallKolonner; j++) {
							if (enUtvei[i][j] == true) {
								Rute rute = new Rute();
								rute.settMerke('o'); // setter inn sort eller hvit rute
								rute.setMaxWidth(Double.MAX_VALUE); // gjør at knappen fyller helt ut
								rute.setStyle("-fx-background-color: #82E0AA");
								rutenett.setHalignment(rute, HPos.CENTER); // sentrerer knappen
								rutenett.add(rute, j, i);
							}
						}
					}

					// hvis det er flere utveier oppdaterer infoteksten
					if (losningString.stoerrelse() > 0) {
						kulisser.getChildren().remove(infoPaaSkjerm);
						infoPaaSkjerm.setText("Viser 1 av "+(losningString.stoerrelse()+1)+" utveier");
						kulisser.getChildren().add(infoPaaSkjerm);
						nesteUtvei.setDisable(false);
					}
					// eller hvis ingen flere utveier
					else if (losningString.stoerrelse() == 0) {
						kulisser.getChildren().remove(infoPaaSkjerm);
						infoPaaSkjerm.setText("Ingen flere utveier");
						kulisser.getChildren().add(infoPaaSkjerm);
						nesteUtvei.setDisable(true);
					}
				}
			}catch (Exception exc) {}
		}
	}


	// viser neste utvei
	class NesteUtveiBehandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			boolean[][] enUtvei;

			// resetter rutenettet
			for (int y = 0; y < antallRader; y++) {
				for (int x = 0; x < antallKolonner; x++) {
					Rute originalTegn = new Rute();
					originalTegn.settMerke(labyrint.ruteListe.get(y).get(x).tilTegn());
					originalTegn.setMaxWidth(Double.MAX_VALUE);
					rutenett.setHalignment(originalTegn, HPos.CENTER);
					rutenett.add(originalTegn, x, y);
				}
			}

			try {

				// henter ut en løsning fra løsningslista
				String liste = losningString.hent(0);
				losningString.fjern(0);

				// henter neste utvei fra lista og viser den
				enUtvei = losningStringTilTabell(liste, antallKolonner, antallRader);
				for (int i = 0; i < antallRader; i++) {
					for (int j = 0; j < antallKolonner; j++) {
						if (enUtvei[i][j] == true) {
							Rute rute = new Rute();
							rute.settMerke('o');
							rute.setMaxWidth(Double.MAX_VALUE);
							rute.setStyle("-fx-background-color: #82E0AA");
							rutenett.setHalignment(rute, HPos.CENTER);
							rutenett.add(rute, j, i);
						}
					}
				}

				// hvis løsningslista er tom byttes infoteksten
				if (losningString.stoerrelse() == 0) {
					kulisser.getChildren().remove(infoPaaSkjerm);
					infoPaaSkjerm.setText("Viser 2 av "+(losningString.stoerrelse()+2)+" utveier");
					kulisser.getChildren().add(infoPaaSkjerm);
					nesteUtvei.setDisable(true);

				}

			} catch (Exception ex) {}
		}
	}

	// Avslutter programmet
	class StoppBehandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}


	// 			---------- Start ----------

	@Override
	public void start(Stage teater) {

		// setter infotext
		infoPaaSkjerm = new Text("Sett et startpunkt!");
		infoPaaSkjerm.setFont(new Font(20));
		infoPaaSkjerm.setX(10); infoPaaSkjerm.setY(30);

		// setter opp en -Neste utvei- knapp
		nesteUtvei = new Button("Neste utvei");
		nesteUtvei.setLayoutX(200); nesteUtvei.setLayoutY(11);
		NesteUtveiBehandler utveiHandler = new NesteUtveiBehandler();
		nesteUtvei.setOnAction(utveiHandler);
		nesteUtvei.setDisable(true);

		// setter opp en -Avslutt- knapp
		Button stoppknapp = new Button("Avslutt");
		stoppknapp.setLayoutX(300); stoppknapp.setLayoutY(11);
		StoppBehandler stopp = new StoppBehandler();
		stoppknapp.setOnAction(stopp);

		File file = new FileChooser().showOpenDialog(teater);

		// henter inn labyrinten
		try {
			labyrint = Labyrint.lesFraFil(file);
		} catch (Exception e) {}

		antallRader = labyrint.antallRader;
		antallKolonner = labyrint.antallKolonner;

		// lister med riktig lengde og tomme ruter
		int antallRuter = antallKolonner * antallRader;
		brett = new Rute[antallRuter + 1];
		KlikkRuteBehandler klikk = new KlikkRuteBehandler();

		// Oppretter klikkbare knapper med riktig tegn og legger dem inn i gridPane
		rutenett = new GridPane();
		rutenett.setGridLinesVisible(true);
		rutenett.setLayoutX(10); rutenett.setLayoutY(50);
		int teller = 0;
		for (int y = 0; y < antallRader; y++) {
			for (int x = 0; x < antallKolonner; x++) {
				brett[teller] = new Rute();
				brett[teller].settMerke(labyrint.ruteListe.get(y).get(x).tilTegn()); // setter inn sort eller hvit rute
				brett[teller].setMaxWidth(Double.MAX_VALUE); // gjør at knappen fyller helt ut
				rutenett.setHalignment(brett[teller], HPos.CENTER); // sentrerer knappen
				rutenett.add(brett[teller], x, y); // legger til knappen i gridpane
				brett[teller].setOnAction(klikk);  // kobler knappen og KlikkRuteBehandler
				teller++;
			}
		}


		kulisser = new Pane();
		//  			  (bredde, høyde)
		kulisser.setPrefSize(900, 900);
		kulisser.getChildren().add(rutenett);
		kulisser.getChildren().add(infoPaaSkjerm);
		kulisser.getChildren().add(stoppknapp);
		kulisser.getChildren().add(nesteUtvei);

		Scene scene = new Scene(kulisser);

		teater.setTitle("Labyrint");
		teater.setScene(scene);
		teater.show();
	}


	// 			---------- Metoder ----------

	// Konverterer losning-String fra oblig 5 til en boolean[][]-representasjon
	// av losningstien.
	static boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
		boolean[][] losning = new boolean[hoyde][bredde];
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
		java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
		while (m.find()) {
			int x = Integer.parseInt(m.group(1));
			int y = Integer.parseInt(m.group(2));
			losning[y][x] = true;
		}
		return losning;
	}
}
