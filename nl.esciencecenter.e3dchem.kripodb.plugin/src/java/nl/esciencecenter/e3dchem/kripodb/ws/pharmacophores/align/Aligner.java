package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores.align;

import nl.esciencecenter.e3dchem.kripodb.ws.client.ApiException;
import nl.esciencecenter.e3dchem.kripodb.ws.client.PharmacophoresApi;
import nl.esciencecenter.e3dchem.kripodb.ws.client.model.AlignedPharmacophore;

public class Aligner {
	private String reference;
	private PharmacophoresApi api;
	private Double cutoff;
	private Integer breakNumCliques;
	private boolean fetchPhar = true;

	public Aligner(String reference, PharmacophoresApi api, Double cutoff, Integer breakNumCliques) {
		this.reference = reference;
		this.api = api;
		this.cutoff = cutoff;
		this.breakNumCliques = breakNumCliques;
	}

	public AlignedPharmacophore align(String probe) throws ApiException {
		return api.alignPharmacophore(reference, probe, cutoff, breakNumCliques, fetchPhar);
	}

}
