package org.genedb.top.install;

interface Server {
    void install(AntBuilder ant, String repository, String target, String port)
}
