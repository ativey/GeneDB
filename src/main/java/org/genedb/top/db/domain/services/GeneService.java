package org.genedb.top.db.domain.services;

import org.genedb.top.db.domain.objects.Gene;

public interface GeneService extends BasicGeneService {
    Gene findGeneByUniqueName(String name);
}
