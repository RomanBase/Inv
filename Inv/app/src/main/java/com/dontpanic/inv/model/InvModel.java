package com.dontpanic.inv.model;

import com.dontpanic.base.model.Model;
import com.dontpanicbase.inv.BR;


public class InvModel extends Model {

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
