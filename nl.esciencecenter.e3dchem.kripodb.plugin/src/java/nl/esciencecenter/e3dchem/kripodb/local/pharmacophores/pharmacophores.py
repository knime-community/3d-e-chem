from kripodb.version import __version_info__
import logging

if int(__version_info__[0]) >= 2 and int(__version_info__[1]) >= 3:
    from kripodb.canned import pharmacophores_by_id
    from kripodb.webservice.client import IncompletePharmacophores

    # Map options to kripo arguments
    id_column = options['id_column']
    pharmacophores_db_filename = options['pharmacophoresdb']

    # Select input column
    queries = input_table[id_column]
    output_table = input_table.copy()

    try:
        phars = pharmacophores_by_id(queries, pharmacophores_db_filename)
        output_table['Pharmacophore'] = phars
    except IncompletePharmacophores as e:
        output_table['Pharmacophore'] = e.fragments
        flow_variables['warning_message'] = e.message
        logging.warning("Following identifier(s) could not be found: " + ",".join(e.absent_identifiers))
else:
    raise NotImplementedError('kripodb Python package too old, requires v2.3.0 or greater, please upgrade')
