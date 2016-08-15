import sygma
import pandas as pd

phase1_cycles = options['phase1_cycles']
phase2_cycles = options['phase2_cycles']
parents_column_name = options['parents_column_name']

scenario = sygma.Scenario([
    [sygma.ruleset['phase1'], phase1_cycles],
    [sygma.ruleset['phase2'], phase2_cycles]
])

parents = input_table[parents_column_name]

metabolites = []
for parent in parents:
    metabolic_tree = scenario.run(parent)
    metabolic_tree.calc_scores()
    metabolites += metabolic_tree.to_list(parent_column=parents_column_name)

metabolites_df = pd.DataFrame(metabolites)
output_table = pd.merge(input_table,
                        metabolites_df,
                        on=parents_column_name)
