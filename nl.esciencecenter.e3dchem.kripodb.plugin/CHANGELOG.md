# Change Log

All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).
The file is formatted as described on http://keepachangelog.com/.

## Unreleased

## [3.0.1] - 2023-11-22

### Changed

- Require KNIME 5.1

## [3.0.0] - 2021-02-12

### Removed

- Python based nodes in favour of web service nodes ([#25](https://github.com/3D-e-Chem/knime-kripodb/issues/25))

## [2.5.0] - 2019-09-25

### Deprecated

- Python based nodes in favour of web service nodes ([#25](https://github.com/3D-e-Chem/knime-kripodb/issues/25))

## [2.4.2] - 2019-07-02

### Changed

- Requires KNIME 4.0 ([#29](https://github.com/3D-e-Chem/knime-kripodb/issues/29))

## [2.4.1] - 2018-04-04

### Fixed

- Last update of web service in node description [#28]

## [2.4.0] - 2018-02-07

### Changed

- Allow KripoDB local nodes to use Python 2 or 3
- Require KNIME 3.5

## [2.3.0] - 2017-07-21

### Added

- Node to fetch pharmacophore from local db file (#12)
- Node to fetch pharmacophore from webservice (#12)

### Changed

- Require KNIME 3.3

## [2.2.1] - 2017-03-07

### Fixes

- Handle nulls in server responses (#23)

## [2.2.0] - 2017-03-05

### Added

- Pure Java nodes to fetch fragment information and similarites from web service (#17)

## Changed

- Python nodes only work for local files, no longer for web service urls

## [2.1.5] - 2017-03-01

Requires KripoDB v2.2.0 or higher.

### Added

- Fragment information node fails completely when just one of the fragment ids is wrong (#11)
- Fragment similarity node gives warnings for query fragment ids that can not be found

## [2.1.4]

Retracted due to unresolved compilation problems.

## [2.1.3] - 2017-01-20

### Added

- kripodb Python installation instruction in node description (#16)
- code coverage (#19)

### Changed

- Allow fragments db to be a webservice url (#15)
- Tests run against mocked web service (#18)

## [2.1.2] - 2016-11-22

### Fixed

- Correct fragments.sqlite url (#14)

## [2.1.1] - 2016-07-18

### Changed

- Nest Kripo nodes under /community/3D-e-Chem (#7)

## [2.1.0] - 2016-07-14

### Added

- Explained fragments db file choices (#6)
- Explained similar fragments matrix file options (#5)

### Changed

- Renamed distance to similarity (#9)

### Removed

- Python templates

## [2.0.1] - 2016-07-11

### Changed

- Moved PythonWrapper classes to own repo (https://github.com/3D-e-Chem/knime-python-wrapper)

## [2.0.0] - 2016-07-06

Version <2.0.0 used Python templates which could be selected as source code and adjusted in a text area in the `Python script` node.
Version >= 2.0.0 uses KripoDB Knime node which have there own dialog with combo boxes and file pickers.

### Added

- Workflow tests
- Run workflow tests on Travis-CI
- Codacy badge
- Check that Python packages are available before executing

### Changed

- Use KripoDB Knime node instead of Python node with a KripoDB template (#3)

## [1.0.3] - 2016-06-21

### Added

- bioisosteric replacement workflow

### Fixed

- Example 'Add fragment for hits' node returns too many rows (#2)

## [1.0.2] - 2016-05-25

### Added

- Use webservice in template and example (#1)

## [1.0.1] - 2016-04-18

### Added

- Distance matrix can be local file or kripodb webservice base url.

### Removed

- DOI for this repo, see https://github.com/3D-e-Chem/kripodb for DOI.

## [1.0.0] - 2016-02-12

### Added

- Python templates to use KripoDB package
- Example workflow on Github repo.

[unreleased]: https://github.com/3D-e-Chem/knime-kripodb/compare/v3.0.1...HEAD
[2.5.0]: https://github.com/3D-e-Chem/knime-kripodb/compare/v3.0.0...v3.0.1
[2.5.0]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.5.0...v3.0.0
[2.5.0]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.4.2...v2.5.0
[2.4.2]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.3.1...v2.4.2
[2.4.1]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.3.0...v2.3.1
[2.3.0]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.2.1...v2.3.0
[2.2.1]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.2.0...v2.2.1
[2.2.0]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.1.5...v2.2.0
[2.1.5]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.1.4...v2.1.5
[2.1.4]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.1.3...v2.1.4
[2.1.3]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.1.2...v2.1.3
[2.1.2]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.1.1...v2.1.2
[2.1.1]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.1.0...v2.1.1
[2.1.0]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.0.1...v2.1.0
[2.0.1]: https://github.com/3D-e-Chem/knime-kripodb/compare/v2.0.0...v2.0.1
[2.0.0]: https://github.com/3D-e-Chem/knime-kripodb/compare/v1.0.3...v2.0.0
[1.0.3]: https://github.com/3D-e-Chem/knime-kripodb/compare/v1.0.2...v1.0.3
[1.0.2]: https://github.com/3D-e-Chem/knime-kripodb/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/3D-e-Chem/knime-kripodb/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/3D-e-Chem/knime-kripodb/releases/tag/v1.0.0
