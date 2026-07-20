/*
 * RomRaider Open-Source Tuning, Logging and Reflashing
 * Copyright (C) 2006-2012 RomRaider.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.romraider.swing;

import static com.romraider.Version.PRODUCT_NAME;
import static com.romraider.util.Platform.LINUX;
import static com.romraider.util.Platform.MAC_OS_X;
import static com.romraider.util.Platform.isPlatform;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;

import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

public final class LookAndFeelManager {
    private static final Logger LOGGER = Logger.getLogger(LookAndFeelManager.class);

    private LookAndFeelManager() {
        throw new UnsupportedOperationException();
    }

    public static void initLookAndFeel() {
        try {
            if (isPlatform(MAC_OS_X)) {
                System.setProperty("apple.awt.rendering", "true");
                System.setProperty("apple.awt.brushMetalLook", "true");
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("apple.awt.window.position.forceSafeCreation", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", PRODUCT_NAME);
            }

            setLookAndFeel(getLookAndFeel());

            // GTKLookAndFeel's buttons carry noticeably more padding than
            // Nimbus/Metal by default, which pushes the toolbars (laid out
            // with FlowLayout and fixed button-count rows tuned for the
            // narrower L&Fs) wide enough to wrap onto a second row. Tighten
            // the default margin globally so button sizing stays close to
            // what those toolbars were tuned for.
            if (isPlatform(LINUX)) {
                UIManager.put("Button.margin", new Insets(1, 1, 1, 1));
            }

            // make sure we have nice window decorations.
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);

        } catch (Exception ex) {
            LOGGER.error("Error loading system look and feel.", ex);
        }
    }

    private static String getLookAndFeel() {
        // Java's "system" L&F detection only recognizes GNOME/GTK sessions
        // on Linux, so it silently falls back to the plain Metal L&F
        // everywhere else (KDE included) rather than throwing. Force
        // GTKLookAndFeel instead: KDE ships its own GTK theme integration
        // (breeze-gtk/kde-gtk-config), so this renders as a proper native
        // dark/light theme there too, not just under GNOME.
        if (isPlatform(LINUX)) return "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        return getSystemLookAndFeelClassName();
    }
}
