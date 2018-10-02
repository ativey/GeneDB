package org.genedb.top.install;

interface DbServer extends Server {
    void loadSchema()
    void loadBootstrapData()
}