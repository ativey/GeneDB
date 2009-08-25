#!/bin/bash

summary() {
    echo "load tmhmm predictions"
}

loaderHelp() {
    cat <<HELP
Load tmhmm predictions, from the file produced by the tmhmm program.

HELP
}

loaderUsage() {
    cat <<USAGE
Usage: `basename $0` pfam -o <organism> -v <version> [-r] [-k key-type] [-f true|false] <file>
Options:
  -o organism
    The common name of the organism. You can get a list of organisms
    by running the command: chado_dump_genome --list
  -v version
    The version of tmhmm that was used to generate the input file
  -x notFoundNotFatal
    If this option is used the loader will continue if it encounters features 
    that are not found in the database. Useful if tmhmm was run against a 
    slightly out of date set of proteins. Default false.
  -r
    Reload. If this option is present, existing tmhmm predictions for
    the specified organism will be deleted before the new ones are loaded.
USAGE
    standard_options
    echo
}

doLoad() {
    organism=''
    programVersion=''
    reload=false
    debug=false
    keyType='polypeptide'
    notFoundNotFatal='false'
    
    OPTIND=0
    while getopts "do:v:rk:x:$stdopts" option; do
        case "$option" in
        d)  debug=true
            ;;
        o)  organism="$OPTARG"
            ;;
        v)  programVersion="$OPTARG"
            ;;
        r)  reload=true
        	echo "Reloading domains: existing tmhmm features will be deleted!"
            ;;
        k)  keyType="$OPTARG"
        	;;
        x)  case "$OPTARG" in
            notFoundNotFatal)
                notFoundNotFatal=true
                ;;
            *)  loaderUsage >&2
                exit 1
                ;;
            esac
            ;;
        *)  process_standard_options "$option"
            ;;
        esac
    done
    shift $[ $OPTIND - 1 ]
    
    if [ -z "$programVersion" -o -z "$organism" ]; then
        loaderUsage >&2
        exit 1
    fi
    
    if [ $# -lt 1 ]; then
        loaderUsage >&2
        exit 1
    fi
    
    read_password
            
    if $reload; then
        java -Xmx256m $database_properties \
            org.genedb.db.loading.auxiliary.ClearTMHMM "$organism"
    fi

    if $debug; then
        echo "Classpath:"
        echo "$CLASSPATH" | perl -0777 -ne 'for (split(/:/,$_)) {print"\t$_\n"}'
        set -x
    fi
            
    java $database_properties \
        org.genedb.db.loading.auxiliary.Load tmhmmloader \
        --tmhmm-version="$programVersion" --not-found-not-fatal=$notFoundNotFatal "$@"
}
