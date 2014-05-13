#!/bin/bash

logecho() {
    echo [`date '+%F %T'`] $1	
}

doeval() {
	command=$1
	logecho $command
	eval $command
	exitCode=$?    
	if [ $exitCode -ne 0 ];then
	    logecho "The script returned a status code of ${exitCode}. Exiting."
	    exit ${exitCode}
	fi
}

BLAST_DB_PATH="/lustre/scratch109/blastdb/Pathogen/website/genedb/"

ORIGINAL_IFS=$IFS
IFS=$'\n'

export PGPASSWORD="genedb"

GET_ORGANISMS_SQL="select distinct(o.common_name) from organism o, feature f where f.organism_id = o.organism_id and o.common_name != 'dummy'"
logecho ${GET_ORGANISMS_SQL}

ORGANISMS_COMMAND="ORGANISMS=\`psql -t -h path-dev-db -U genedb -c \"${GET_ORGANISMS_SQL}\" nightly\`"
doeval $ORGANISMS_COMMAND

CONNECTION_DETAILS="-d nightly -u genedb -p 5432 -i path-dev-db"
    

for organism in $ORGANISMS
do
	#regex / / to trim white spaces
	organism=${organism/ /} 
	if [[ $organism != 'dummy' ]]; then
		
     	logecho "Dumping ${organism} : "
     	
     	DUMP_PROTEINS="chado_dump_proteins -s -o ${organism} ${CONNECTION_DETAILS} > ${BLAST_DB_PATH}GeneDB_${organism}_Proteins"
     	doeval $DUMP_PROTEINS
     	
     	DUMP_TRANSCRIPTS="chado_dump_transcripts -o ${organism} ${CONNECTION_DETAILS} > ${BLAST_DB_PATH}GeneDB_${organism}_Genes"
     	doeval $DUMP_TRANSCRIPTS
     	
     	DUMP_GENOME="chado_dump_genome -o ${organism} ${CONNECTION_DETAILS} > ${BLAST_DB_PATH}GeneDB_${organism}_Contigs"
     	doeval $DUMP_GENOME
     	
     	logecho "Dumped!"
     	
    fi
done



    
