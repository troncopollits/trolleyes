package net.daw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.daw.bean.TipousuarioBean;

public class TipousuarioDao {

	Connection oConnection;
	String ob = "tipoUsuario";

	public TipousuarioDao(Connection oConnection) {
		super();
		this.oConnection = oConnection;
	}

	// Metodo que retorna el registro seleccionado. FUNCIONA
	public TipousuarioBean get(int id) throws Exception {
		String strSQL = "SELECT * FROM " + ob + " WHERE id=?";
		TipousuarioBean oTipousuarioBean;
		ResultSet oResultSet = null;
		PreparedStatement oPreparedStatement = null;
		try {
			oPreparedStatement = oConnection.prepareStatement(strSQL);
			oPreparedStatement.setInt(1, id);
			oResultSet = oPreparedStatement.executeQuery();
			if (oResultSet.next()) {
				oTipousuarioBean = new TipousuarioBean();
				oTipousuarioBean.setId(oResultSet.getInt("id"));
				oTipousuarioBean.setDesc(oResultSet.getString("desc"));
			} else {
				oTipousuarioBean = null;
			}
		} catch (SQLException e) {
			throw new Exception("Error en Dao get de tipousuario", e);
		} finally {
			if (oResultSet != null) {
				oResultSet.close();
			}
			if (oPreparedStatement != null) {
				oPreparedStatement.close();
			}
		}

		return oTipousuarioBean;

	}		
	
	//Retorna cuantos usuarios hay, NO FUNCIONA
	@SuppressWarnings("null")
	public TipousuarioBean getCount() throws Exception {
		String strSQL = "SELECT COUNT(id) FROM " + ob;
		TipousuarioBean oTipousuarioBean = null;
		ResultSet oResultSet = null;
		PreparedStatement oPreparedStatement = null;
		try {
			oPreparedStatement = oConnection.prepareStatement(strSQL);
			oResultSet = oPreparedStatement.executeQuery(strSQL);
			//Next busca el siguiente objeto en la base de datos
			if(oResultSet.next()) {
				oTipousuarioBean.setId(oResultSet.getInt(1));
				oTipousuarioBean.setDesc("Total registrados.");
			}else {
				oTipousuarioBean = null;
			}
		}catch(SQLException e) {
			throw new Exception("Error en metodo getCount.", e);
		}finally {
			// Obligatorio cerrar la peticion para no colapsar el programa
			if (oPreparedStatement != null) {
				oPreparedStatement.close();
			}
			if (oResultSet != null) {
                oResultSet.close();
            }
		}
		
		return oTipousuarioBean;
	}
	
	//Metodo actualizar, FUNCIONA
		@SuppressWarnings("null")
		public TipousuarioBean update(String desc, Integer id) throws Exception {
			String strSQL = "UPDATE "+ ob + " SET `desc`=? WHERE `id`=? ";
			TipousuarioBean oTipousuarioBean = null;
			PreparedStatement oPreparedStatement = null;
			int envio;
			try {
				oPreparedStatement = oConnection.prepareStatement(strSQL);
				oPreparedStatement.setString(1, desc);
				oPreparedStatement.setInt(2, id);
				envio = oPreparedStatement.executeUpdate();
				if(envio == 1) {
					oTipousuarioBean = this.get(id);
				}else {
					oTipousuarioBean.setId(500);
					oTipousuarioBean.setDesc("Error actualizando la bd.");
				}
			}catch (SQLException e) {
				throw new Exception("Fallo en método update.", e);
			} finally {
				// Obligatorio cerrar la peticion para no colapsar el programa
				if (oPreparedStatement != null) {
					oPreparedStatement.close();
				}
			}
			return oTipousuarioBean;
		}
		
	@SuppressWarnings("null")
	public TipousuarioBean create(String desc) throws Exception {
		String strSQL = "INSERT INTO " + ob + " `(desc)` VALUES (?)";
		TipousuarioBean oTipousuarioBean = null;
		ResultSet oResultSet = null;
		PreparedStatement oPreparedStatement = null;
		int envio;
		try {
            oPreparedStatement = oConnection.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
            oPreparedStatement.setString(1, desc);
            envio = oPreparedStatement.executeUpdate();
            if(envio == 1) {
            	oResultSet = oPreparedStatement.getGeneratedKeys();
            	if(oResultSet.next()) {
            		oTipousuarioBean.setId(oResultSet.getInt(1));
            		oTipousuarioBean.setDesc(desc);
            	}else {
            		oTipousuarioBean.setId(500);
            		oTipousuarioBean.setDesc("Error en la creación.");
            	}
            }
		}catch (SQLException e) {
            throw new Exception("Error en metodo create: " + e.getMessage(), e);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
		return oTipousuarioBean;
	}
	
	

	// Metodo borrar FUNCIONA
	public TipousuarioBean delete(Integer id) throws Exception {
		String strSQL = "DELETE FROM " + ob + " WHERE id=?";
		TipousuarioBean oTipousuarioBean = new TipousuarioBean();
		PreparedStatement oPreparedStatement = null;

		try {
			// Comprobamos si existe o no el registro que queremos eliminar
			if (this.get(id) == null) {
				oTipousuarioBean.setId(id);
				oTipousuarioBean.setDesc("No se encuentran coicidencias.");
			} else {
				oPreparedStatement = oConnection.prepareStatement(strSQL);
				oPreparedStatement.setInt(1, id);
				oPreparedStatement.execute();

				oTipousuarioBean.setId(id);
				oTipousuarioBean.setDesc("Eliminado con éxito.");
			}
		} catch (SQLException e) {
			throw new Exception("Error en metodo delete.", e);
		} finally {
			// Obligatorio cerrar la peticion para no colapsar el programa
			if (oPreparedStatement != null) {
				oPreparedStatement.close();
			}
		}
		return oTipousuarioBean;

	}
}
