package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores.align;

import nl.esciencecenter.e3dchem.kripodb.ws.client.ApiException;
import nl.esciencecenter.e3dchem.kripodb.ws.client.PharmacophoresApi;
import nl.esciencecenter.e3dchem.kripodb.ws.client.model.AlignedPharmacophore;

public class Aligner {
	private String reference;
	private PharmacophoresApi api;
	private Double cutoff = 1.0;
	private Integer breakNumCliques;

	public Aligner(String reference, PharmacophoresApi api) {
		this.reference = reference;
		this.api = api;
	}

	public AlignedPharmacophore align(String probe) throws ApiException {
		return api.alignPharmacophore(reference, probe, cutoff, breakNumCliques, true);
	}

}
