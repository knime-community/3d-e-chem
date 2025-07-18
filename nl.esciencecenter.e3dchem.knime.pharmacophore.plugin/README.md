[KNIME](https://www.knime.com) plugin with nodes to convert and align pharmacophores.


[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.997332.svg)](https://doi.org/10.5281/zenodo.997332)

A pharmacophore is an abstract description of molecular features that are necessary for molecular recognition of a ligand by a biological macromolecule.
Nodes in this plugin allow for converting pharmacophores, from and to molecules, by mapping elements to pharmacophore type and, reading from or writing to the phar file format used by the [Silicos IT align-it tool](http://silicos-it.be.s3-website-eu-west-1.amazonaws.com/software/align-it/1.0.4/align-it.html#pharmacophores).
This plugin adds the Pharmacophore (Phar) data type to KNIME, allowing nodes to read/write/manipulate pharmacophores inside KNIME like the [Silicos-it align-it](https://github.com/3D-e-Chem/knime-silicos-it), [Kripo pharmacophore retrieval](https://github.com/3D-e-Chem/knime-kripodb) and [molviewer pharmacophore viewer](https://github.com/3D-e-Chem/knime-molviewer) nodes.

This project uses [Eclipse Tycho](https://www.eclipse.org/tycho/) to perform build steps.
