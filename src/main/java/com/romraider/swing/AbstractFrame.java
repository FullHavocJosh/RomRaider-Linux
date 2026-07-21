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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public abstract class AbstractFrame extends JFrame implements WindowListener, PropertyChangeListener {
    public AbstractFrame() throws HeadlessException {
        super();
        installMaximizeResizeWorkaround();
    }

    public AbstractFrame(String arg0) throws HeadlessException {
        super(arg0);
        installMaximizeResizeWorkaround();
    }

    // Some XWayland/compositor setups (seen under a KDE Plasma Wayland
    // session) deliver the WINDOW_STATE_CHANGED notification when the
    // window manager maximizes a window, but never send the corresponding
    // ConfigureNotify with the new geometry. That leaves AWT's own
    // size/bounds model stuck at the pre-maximize size even though the
    // window is now visibly maximized, so Swing's layout never re-flows
    // into the new space. Detect that mismatch and force the frame's
    // bounds back in sync with the real maximized screen area.
    private void installMaximizeResizeWorkaround() {
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if ((e.getNewState() & MAXIMIZED_BOTH) != MAXIMIZED_BOTH) {
                    return;
                }
                // Defer to the next event-dispatch cycle: this can fire
                // before the frame's menu bar/toolbar/content have been
                // fully built (e.g. ECUEditor's constructor returns and
                // shows the frame before ECUExec calls
                // initializeEditorUI() on it), and correcting the bounds
                // against a still-incomplete component tree left the
                // window blank rather than fixed.
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        GraphicsConfiguration gc = getGraphicsConfiguration();
                        if (gc == null) {
                            return;
                        }
                        Rectangle screenBounds = gc.getBounds();
                        Insets screenInsets = getToolkit().getScreenInsets(gc);
                        Rectangle maximizedBounds = new Rectangle(
                                screenBounds.x + screenInsets.left,
                                screenBounds.y + screenInsets.top,
                                screenBounds.width - screenInsets.left - screenInsets.right,
                                screenBounds.height - screenInsets.top - screenInsets.bottom);
                        if (!getBounds().equals(maximizedBounds)) {
                            setBounds(maximizedBounds);
                            invalidate();
                            validate();
                            repaint();
                        }
                    }
                });
            }
        });
    }

    private static final long serialVersionUID = 7948304087075622157L;

    public void windowActivated(WindowEvent arg0) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void propertyChange(PropertyChangeEvent arg0) {
    }

}
