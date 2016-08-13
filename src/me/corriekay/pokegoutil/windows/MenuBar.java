package me.corriekay.pokegoutil.windows;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Stats;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.player.PlayerProfile.Currency;
import me.corriekay.pokegoutil.BlossomsPoGoManager;
import me.corriekay.pokegoutil.DATA.controllers.AccountController;
import me.corriekay.pokegoutil.utils.Config;
import me.corriekay.pokegoutil.utils.pokemon.PokemonUtils;

import javax.swing.*;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private final PokemonGo go;

	public MenuBar(PokemonGo go) {
		this.go = go;

		JMenu file, help;

		// File
		file = new JMenu("File");

		JMenuItem trainerStats = new JMenuItem("View Trainer Stats");
		trainerStats.addActionListener(al -> {
			try {
				displayTrainerStats();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		file.add(trainerStats);

		JCheckBoxMenuItem tAfterE = new JCheckBoxMenuItem("Transfer After Evolve");
		tAfterE.setSelected(Config.getConfig().getBool("transfer.afterEvolve", false));
		tAfterE.addItemListener(e -> Config.getConfig().setBool("transfer.afterEvolve", tAfterE.isSelected()));

		file.add(tAfterE);

		JCheckBoxMenuItem doNotShowBulkPopup = new JCheckBoxMenuItem("Show Bulk Completion");
		doNotShowBulkPopup.setSelected(Config.getConfig().getBool("popup.afterBulk", true));
		doNotShowBulkPopup
				.addItemListener(e -> Config.getConfig().setBool("popup.afterBulk", doNotShowBulkPopup.isSelected()));

		file.add(doNotShowBulkPopup);

		JMenuItem logout = new JMenuItem("Logout");
		logout.addActionListener(al -> {
			try {
				logout();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		file.add(logout);

		add(file);

		help = new JMenu("Help");

		JMenuItem about = new JMenuItem("About");
		about.addActionListener(l -> JOptionPane.showMessageDialog(null,
				"Version: " + BlossomsPoGoManager.VERSION + "\n\nAuthor: Corrie 'Blossom' Kay"
						+ "\n\nThis work is protected under the" + "\nCreative Commons Attribution-"
						+ "\nNonCommercial-ShareAlike 4.0" + "\nInternational license, which can" + "\nbe found here:"
						+ "\nhttps://creativecommons.org/" + "\nlicenses/by-nc-sa/4.0/"
						+ "\n\nThanks to Grover for providing" + "\nsuch a great API." + "\n\nThanks for Draseart for"
						+ "\nthe icon art.",
				"About Blossom's Pokémon Go Manager", JOptionPane.PLAIN_MESSAGE));

		help.add(about);

		add(help);
	}

	private void logout() throws Exception {
		AccountController.logOff();
	}

	private void displayTrainerStats() throws Exception {
		go.getInventories().updateInventories(true);
		PlayerProfile pp = go.getPlayerProfile();
		Stats stats = pp.getStats();
		Object[] tstats = { "Trainer Name: " + pp.getPlayerData().getUsername(),
				"Team: " + PokemonUtils.convertTeamColorToName(pp.getPlayerData().getTeamValue()),
				"Level: " + stats.getLevel(), "XP: " + stats.getExperience() + " ("
						+ (stats.getNextLevelXp() - stats.getExperience()) + " to next level)",
				"Stardust: " + pp.getCurrency(Currency.STARDUST) };
		JOptionPane.showMessageDialog(null, tstats, "Trainer Stats", JOptionPane.PLAIN_MESSAGE);
	}

}
