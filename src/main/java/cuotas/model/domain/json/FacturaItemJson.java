package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.factura.FacturaItem;

public class FacturaItemJson extends GenericJson<FacturaItem> {

	@Override
	public FacturaItem fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	protected JSONObject getJson(FacturaItem facturaItem) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("codigo", facturaItem.getCodigo());
		jsonObj.put("cantidad", facturaItem.getCantidad());
		jsonObj.put("precio", facturaItem.getPrecio());
		jsonObj.put("precioTotal", facturaItem.getPrecioTotal());
		jsonObj.put("facturaItemTipo", FactoryJson.facturaItemTipoJson.getJson(facturaItem.getFacturaItemTipo()));
		return jsonObj;
	}

}
