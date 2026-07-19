# RomRaider

RomRaider is a free, open source tuning suite created for viewing, logging and
tuning of modern Subaru Engine Control Units. The intuitive tuning interface
and powerful datalogger are modelled to be familiar to experienced professional
tuners while providing all the power of expensive commercial products, without
license fees.

See Building_RomRaider.txt for information on generating usable binaries.

## Linux packaging

This fork adds native Linux packaging on top of upstream RomRaider: `.rpm`
(Fedora/RHEL), `.deb` (Debian/Ubuntu), and a Flatpak bundle. Releases are
published automatically on the [Releases page](../../releases) whenever a
`v*` tag is pushed. For Windows builds, use the upstream
[RomRaider/RomRaider](https://github.com/RomRaider/RomRaider) repo.

### Known limitation: Innovate LM2 via MTS Link

The Innovate LM2 external logger plugin (`Lm2MtsDataSource`) talks to
Innovate's MTS Link software through `com4j`, a Java-to-COM bridge — COM is
a Windows-only technology, so **this specific plugin does not work on
Linux**, on any of the Linux packages above. It fails gracefully (logged as
an error, doesn't crash the app) and every other external logger plugin
(AEM, PLX, 14Point7, Phidgets, Zeitronix, TechEdge, etc.) is unaffected,
since those use serial/USB rather than Windows COM.

See the following links for further information:

- http://www.romraider.com/
- http://www.romraider.com/forum/
- https://github.com/RomRaider/RomRaider
