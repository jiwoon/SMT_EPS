package com.jimi.smt.eps.serversocket.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgramExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ProgramExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * 
     * 
     * @author wcyong
     * 
     * @date 2018-07-19
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andFileNameIsNull() {
            addCriterion("file_name is null");
            return (Criteria) this;
        }

        public Criteria andFileNameIsNotNull() {
            addCriterion("file_name is not null");
            return (Criteria) this;
        }

        public Criteria andFileNameEqualTo(String value) {
            addCriterion("file_name =", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotEqualTo(String value) {
            addCriterion("file_name <>", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameGreaterThan(String value) {
            addCriterion("file_name >", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("file_name >=", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLessThan(String value) {
            addCriterion("file_name <", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLessThanOrEqualTo(String value) {
            addCriterion("file_name <=", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLike(String value) {
            addCriterion("file_name like", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotLike(String value) {
            addCriterion("file_name not like", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameIn(List<String> values) {
            addCriterion("file_name in", values, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotIn(List<String> values) {
            addCriterion("file_name not in", values, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameBetween(String value1, String value2) {
            addCriterion("file_name between", value1, value2, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotBetween(String value1, String value2) {
            addCriterion("file_name not between", value1, value2, "fileName");
            return (Criteria) this;
        }

        public Criteria andClientIsNull() {
            addCriterion("client is null");
            return (Criteria) this;
        }

        public Criteria andClientIsNotNull() {
            addCriterion("client is not null");
            return (Criteria) this;
        }

        public Criteria andClientEqualTo(String value) {
            addCriterion("client =", value, "client");
            return (Criteria) this;
        }

        public Criteria andClientNotEqualTo(String value) {
            addCriterion("client <>", value, "client");
            return (Criteria) this;
        }

        public Criteria andClientGreaterThan(String value) {
            addCriterion("client >", value, "client");
            return (Criteria) this;
        }

        public Criteria andClientGreaterThanOrEqualTo(String value) {
            addCriterion("client >=", value, "client");
            return (Criteria) this;
        }

        public Criteria andClientLessThan(String value) {
            addCriterion("client <", value, "client");
            return (Criteria) this;
        }

        public Criteria andClientLessThanOrEqualTo(String value) {
            addCriterion("client <=", value, "client");
            return (Criteria) this;
        }

        public Criteria andClientLike(String value) {
            addCriterion("client like", value, "client");
            return (Criteria) this;
        }

        public Criteria andClientNotLike(String value) {
            addCriterion("client not like", value, "client");
            return (Criteria) this;
        }

        public Criteria andClientIn(List<String> values) {
            addCriterion("client in", values, "client");
            return (Criteria) this;
        }

        public Criteria andClientNotIn(List<String> values) {
            addCriterion("client not in", values, "client");
            return (Criteria) this;
        }

        public Criteria andClientBetween(String value1, String value2) {
            addCriterion("client between", value1, value2, "client");
            return (Criteria) this;
        }

        public Criteria andClientNotBetween(String value1, String value2) {
            addCriterion("client not between", value1, value2, "client");
            return (Criteria) this;
        }

        public Criteria andMachineNameIsNull() {
            addCriterion("machine_name is null");
            return (Criteria) this;
        }

        public Criteria andMachineNameIsNotNull() {
            addCriterion("machine_name is not null");
            return (Criteria) this;
        }

        public Criteria andMachineNameEqualTo(String value) {
            addCriterion("machine_name =", value, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameNotEqualTo(String value) {
            addCriterion("machine_name <>", value, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameGreaterThan(String value) {
            addCriterion("machine_name >", value, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameGreaterThanOrEqualTo(String value) {
            addCriterion("machine_name >=", value, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameLessThan(String value) {
            addCriterion("machine_name <", value, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameLessThanOrEqualTo(String value) {
            addCriterion("machine_name <=", value, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameLike(String value) {
            addCriterion("machine_name like", value, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameNotLike(String value) {
            addCriterion("machine_name not like", value, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameIn(List<String> values) {
            addCriterion("machine_name in", values, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameNotIn(List<String> values) {
            addCriterion("machine_name not in", values, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameBetween(String value1, String value2) {
            addCriterion("machine_name between", value1, value2, "machineName");
            return (Criteria) this;
        }

        public Criteria andMachineNameNotBetween(String value1, String value2) {
            addCriterion("machine_name not between", value1, value2, "machineName");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(String value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(String value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(String value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(String value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(String value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(String value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLike(String value) {
            addCriterion("version like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotLike(String value) {
            addCriterion("version not like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<String> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<String> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(String value1, String value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(String value1, String value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andMachineConfigIsNull() {
            addCriterion("machine_config is null");
            return (Criteria) this;
        }

        public Criteria andMachineConfigIsNotNull() {
            addCriterion("machine_config is not null");
            return (Criteria) this;
        }

        public Criteria andMachineConfigEqualTo(String value) {
            addCriterion("machine_config =", value, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigNotEqualTo(String value) {
            addCriterion("machine_config <>", value, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigGreaterThan(String value) {
            addCriterion("machine_config >", value, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigGreaterThanOrEqualTo(String value) {
            addCriterion("machine_config >=", value, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigLessThan(String value) {
            addCriterion("machine_config <", value, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigLessThanOrEqualTo(String value) {
            addCriterion("machine_config <=", value, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigLike(String value) {
            addCriterion("machine_config like", value, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigNotLike(String value) {
            addCriterion("machine_config not like", value, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigIn(List<String> values) {
            addCriterion("machine_config in", values, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigNotIn(List<String> values) {
            addCriterion("machine_config not in", values, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigBetween(String value1, String value2) {
            addCriterion("machine_config between", value1, value2, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andMachineConfigNotBetween(String value1, String value2) {
            addCriterion("machine_config not between", value1, value2, "machineConfig");
            return (Criteria) this;
        }

        public Criteria andProgramNoIsNull() {
            addCriterion("program_no is null");
            return (Criteria) this;
        }

        public Criteria andProgramNoIsNotNull() {
            addCriterion("program_no is not null");
            return (Criteria) this;
        }

        public Criteria andProgramNoEqualTo(String value) {
            addCriterion("program_no =", value, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoNotEqualTo(String value) {
            addCriterion("program_no <>", value, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoGreaterThan(String value) {
            addCriterion("program_no >", value, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoGreaterThanOrEqualTo(String value) {
            addCriterion("program_no >=", value, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoLessThan(String value) {
            addCriterion("program_no <", value, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoLessThanOrEqualTo(String value) {
            addCriterion("program_no <=", value, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoLike(String value) {
            addCriterion("program_no like", value, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoNotLike(String value) {
            addCriterion("program_no not like", value, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoIn(List<String> values) {
            addCriterion("program_no in", values, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoNotIn(List<String> values) {
            addCriterion("program_no not in", values, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoBetween(String value1, String value2) {
            addCriterion("program_no between", value1, value2, "programNo");
            return (Criteria) this;
        }

        public Criteria andProgramNoNotBetween(String value1, String value2) {
            addCriterion("program_no not between", value1, value2, "programNo");
            return (Criteria) this;
        }

        public Criteria andLineIsNull() {
            addCriterion("line is null");
            return (Criteria) this;
        }

        public Criteria andLineIsNotNull() {
            addCriterion("line is not null");
            return (Criteria) this;
        }

        public Criteria andLineEqualTo(String value) {
            addCriterion("line =", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineNotEqualTo(String value) {
            addCriterion("line <>", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineGreaterThan(String value) {
            addCriterion("line >", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineGreaterThanOrEqualTo(String value) {
            addCriterion("line >=", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineLessThan(String value) {
            addCriterion("line <", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineLessThanOrEqualTo(String value) {
            addCriterion("line <=", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineLike(String value) {
            addCriterion("line like", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineNotLike(String value) {
            addCriterion("line not like", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineIn(List<String> values) {
            addCriterion("line in", values, "line");
            return (Criteria) this;
        }

        public Criteria andLineNotIn(List<String> values) {
            addCriterion("line not in", values, "line");
            return (Criteria) this;
        }

        public Criteria andLineBetween(String value1, String value2) {
            addCriterion("line between", value1, value2, "line");
            return (Criteria) this;
        }

        public Criteria andLineNotBetween(String value1, String value2) {
            addCriterion("line not between", value1, value2, "line");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateIsNull() {
            addCriterion("effective_date is null");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateIsNotNull() {
            addCriterion("effective_date is not null");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateEqualTo(String value) {
            addCriterion("effective_date =", value, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateNotEqualTo(String value) {
            addCriterion("effective_date <>", value, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateGreaterThan(String value) {
            addCriterion("effective_date >", value, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateGreaterThanOrEqualTo(String value) {
            addCriterion("effective_date >=", value, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateLessThan(String value) {
            addCriterion("effective_date <", value, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateLessThanOrEqualTo(String value) {
            addCriterion("effective_date <=", value, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateLike(String value) {
            addCriterion("effective_date like", value, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateNotLike(String value) {
            addCriterion("effective_date not like", value, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateIn(List<String> values) {
            addCriterion("effective_date in", values, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateNotIn(List<String> values) {
            addCriterion("effective_date not in", values, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateBetween(String value1, String value2) {
            addCriterion("effective_date between", value1, value2, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveDateNotBetween(String value1, String value2) {
            addCriterion("effective_date not between", value1, value2, "effectiveDate");
            return (Criteria) this;
        }

        public Criteria andPcbNoIsNull() {
            addCriterion("PCB_no is null");
            return (Criteria) this;
        }

        public Criteria andPcbNoIsNotNull() {
            addCriterion("PCB_no is not null");
            return (Criteria) this;
        }

        public Criteria andPcbNoEqualTo(String value) {
            addCriterion("PCB_no =", value, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoNotEqualTo(String value) {
            addCriterion("PCB_no <>", value, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoGreaterThan(String value) {
            addCriterion("PCB_no >", value, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoGreaterThanOrEqualTo(String value) {
            addCriterion("PCB_no >=", value, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoLessThan(String value) {
            addCriterion("PCB_no <", value, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoLessThanOrEqualTo(String value) {
            addCriterion("PCB_no <=", value, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoLike(String value) {
            addCriterion("PCB_no like", value, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoNotLike(String value) {
            addCriterion("PCB_no not like", value, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoIn(List<String> values) {
            addCriterion("PCB_no in", values, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoNotIn(List<String> values) {
            addCriterion("PCB_no not in", values, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoBetween(String value1, String value2) {
            addCriterion("PCB_no between", value1, value2, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andPcbNoNotBetween(String value1, String value2) {
            addCriterion("PCB_no not between", value1, value2, "pcbNo");
            return (Criteria) this;
        }

        public Criteria andBomIsNull() {
            addCriterion("BOM is null");
            return (Criteria) this;
        }

        public Criteria andBomIsNotNull() {
            addCriterion("BOM is not null");
            return (Criteria) this;
        }

        public Criteria andBomEqualTo(String value) {
            addCriterion("BOM =", value, "bom");
            return (Criteria) this;
        }

        public Criteria andBomNotEqualTo(String value) {
            addCriterion("BOM <>", value, "bom");
            return (Criteria) this;
        }

        public Criteria andBomGreaterThan(String value) {
            addCriterion("BOM >", value, "bom");
            return (Criteria) this;
        }

        public Criteria andBomGreaterThanOrEqualTo(String value) {
            addCriterion("BOM >=", value, "bom");
            return (Criteria) this;
        }

        public Criteria andBomLessThan(String value) {
            addCriterion("BOM <", value, "bom");
            return (Criteria) this;
        }

        public Criteria andBomLessThanOrEqualTo(String value) {
            addCriterion("BOM <=", value, "bom");
            return (Criteria) this;
        }

        public Criteria andBomLike(String value) {
            addCriterion("BOM like", value, "bom");
            return (Criteria) this;
        }

        public Criteria andBomNotLike(String value) {
            addCriterion("BOM not like", value, "bom");
            return (Criteria) this;
        }

        public Criteria andBomIn(List<String> values) {
            addCriterion("BOM in", values, "bom");
            return (Criteria) this;
        }

        public Criteria andBomNotIn(List<String> values) {
            addCriterion("BOM not in", values, "bom");
            return (Criteria) this;
        }

        public Criteria andBomBetween(String value1, String value2) {
            addCriterion("BOM between", value1, value2, "bom");
            return (Criteria) this;
        }

        public Criteria andBomNotBetween(String value1, String value2) {
            addCriterion("BOM not between", value1, value2, "bom");
            return (Criteria) this;
        }

        public Criteria andProgramNameIsNull() {
            addCriterion("program_name is null");
            return (Criteria) this;
        }

        public Criteria andProgramNameIsNotNull() {
            addCriterion("program_name is not null");
            return (Criteria) this;
        }

        public Criteria andProgramNameEqualTo(String value) {
            addCriterion("program_name =", value, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameNotEqualTo(String value) {
            addCriterion("program_name <>", value, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameGreaterThan(String value) {
            addCriterion("program_name >", value, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameGreaterThanOrEqualTo(String value) {
            addCriterion("program_name >=", value, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameLessThan(String value) {
            addCriterion("program_name <", value, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameLessThanOrEqualTo(String value) {
            addCriterion("program_name <=", value, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameLike(String value) {
            addCriterion("program_name like", value, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameNotLike(String value) {
            addCriterion("program_name not like", value, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameIn(List<String> values) {
            addCriterion("program_name in", values, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameNotIn(List<String> values) {
            addCriterion("program_name not in", values, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameBetween(String value1, String value2) {
            addCriterion("program_name between", value1, value2, "programName");
            return (Criteria) this;
        }

        public Criteria andProgramNameNotBetween(String value1, String value2) {
            addCriterion("program_name not between", value1, value2, "programName");
            return (Criteria) this;
        }

        public Criteria andAuditorIsNull() {
            addCriterion("auditor is null");
            return (Criteria) this;
        }

        public Criteria andAuditorIsNotNull() {
            addCriterion("auditor is not null");
            return (Criteria) this;
        }

        public Criteria andAuditorEqualTo(String value) {
            addCriterion("auditor =", value, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorNotEqualTo(String value) {
            addCriterion("auditor <>", value, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorGreaterThan(String value) {
            addCriterion("auditor >", value, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorGreaterThanOrEqualTo(String value) {
            addCriterion("auditor >=", value, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorLessThan(String value) {
            addCriterion("auditor <", value, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorLessThanOrEqualTo(String value) {
            addCriterion("auditor <=", value, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorLike(String value) {
            addCriterion("auditor like", value, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorNotLike(String value) {
            addCriterion("auditor not like", value, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorIn(List<String> values) {
            addCriterion("auditor in", values, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorNotIn(List<String> values) {
            addCriterion("auditor not in", values, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorBetween(String value1, String value2) {
            addCriterion("auditor between", value1, value2, "auditor");
            return (Criteria) this;
        }

        public Criteria andAuditorNotBetween(String value1, String value2) {
            addCriterion("auditor not between", value1, value2, "auditor");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andWorkOrderIsNull() {
            addCriterion("work_order is null");
            return (Criteria) this;
        }

        public Criteria andWorkOrderIsNotNull() {
            addCriterion("work_order is not null");
            return (Criteria) this;
        }

        public Criteria andWorkOrderEqualTo(String value) {
            addCriterion("work_order =", value, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderNotEqualTo(String value) {
            addCriterion("work_order <>", value, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderGreaterThan(String value) {
            addCriterion("work_order >", value, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderGreaterThanOrEqualTo(String value) {
            addCriterion("work_order >=", value, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderLessThan(String value) {
            addCriterion("work_order <", value, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderLessThanOrEqualTo(String value) {
            addCriterion("work_order <=", value, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderLike(String value) {
            addCriterion("work_order like", value, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderNotLike(String value) {
            addCriterion("work_order not like", value, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderIn(List<String> values) {
            addCriterion("work_order in", values, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderNotIn(List<String> values) {
            addCriterion("work_order not in", values, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderBetween(String value1, String value2) {
            addCriterion("work_order between", value1, value2, "workOrder");
            return (Criteria) this;
        }

        public Criteria andWorkOrderNotBetween(String value1, String value2) {
            addCriterion("work_order not between", value1, value2, "workOrder");
            return (Criteria) this;
        }

        public Criteria andBoardTypeIsNull() {
            addCriterion("board_type is null");
            return (Criteria) this;
        }

        public Criteria andBoardTypeIsNotNull() {
            addCriterion("board_type is not null");
            return (Criteria) this;
        }

        public Criteria andBoardTypeEqualTo(Integer value) {
            addCriterion("board_type =", value, "boardType");
            return (Criteria) this;
        }

        public Criteria andBoardTypeNotEqualTo(Integer value) {
            addCriterion("board_type <>", value, "boardType");
            return (Criteria) this;
        }

        public Criteria andBoardTypeGreaterThan(Integer value) {
            addCriterion("board_type >", value, "boardType");
            return (Criteria) this;
        }

        public Criteria andBoardTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("board_type >=", value, "boardType");
            return (Criteria) this;
        }

        public Criteria andBoardTypeLessThan(Integer value) {
            addCriterion("board_type <", value, "boardType");
            return (Criteria) this;
        }

        public Criteria andBoardTypeLessThanOrEqualTo(Integer value) {
            addCriterion("board_type <=", value, "boardType");
            return (Criteria) this;
        }

        public Criteria andBoardTypeIn(List<Integer> values) {
            addCriterion("board_type in", values, "boardType");
            return (Criteria) this;
        }

        public Criteria andBoardTypeNotIn(List<Integer> values) {
            addCriterion("board_type not in", values, "boardType");
            return (Criteria) this;
        }

        public Criteria andBoardTypeBetween(Integer value1, Integer value2) {
            addCriterion("board_type between", value1, value2, "boardType");
            return (Criteria) this;
        }

        public Criteria andBoardTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("board_type not between", value1, value2, "boardType");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Integer value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Integer value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Integer value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Integer value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Integer value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Integer> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Integer> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Integer value1, Integer value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Integer value1, Integer value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStructureIsNull() {
            addCriterion("structure is null");
            return (Criteria) this;
        }

        public Criteria andStructureIsNotNull() {
            addCriterion("structure is not null");
            return (Criteria) this;
        }

        public Criteria andStructureEqualTo(Integer value) {
            addCriterion("structure =", value, "structure");
            return (Criteria) this;
        }

        public Criteria andStructureNotEqualTo(Integer value) {
            addCriterion("structure <>", value, "structure");
            return (Criteria) this;
        }

        public Criteria andStructureGreaterThan(Integer value) {
            addCriterion("structure >", value, "structure");
            return (Criteria) this;
        }

        public Criteria andStructureGreaterThanOrEqualTo(Integer value) {
            addCriterion("structure >=", value, "structure");
            return (Criteria) this;
        }

        public Criteria andStructureLessThan(Integer value) {
            addCriterion("structure <", value, "structure");
            return (Criteria) this;
        }

        public Criteria andStructureLessThanOrEqualTo(Integer value) {
            addCriterion("structure <=", value, "structure");
            return (Criteria) this;
        }

        public Criteria andStructureIn(List<Integer> values) {
            addCriterion("structure in", values, "structure");
            return (Criteria) this;
        }

        public Criteria andStructureNotIn(List<Integer> values) {
            addCriterion("structure not in", values, "structure");
            return (Criteria) this;
        }

        public Criteria andStructureBetween(Integer value1, Integer value2) {
            addCriterion("structure between", value1, value2, "structure");
            return (Criteria) this;
        }

        public Criteria andStructureNotBetween(Integer value1, Integer value2) {
            addCriterion("structure not between", value1, value2, "structure");
            return (Criteria) this;
        }

        public Criteria andPlanProductIsNull() {
            addCriterion("plan_product is null");
            return (Criteria) this;
        }

        public Criteria andPlanProductIsNotNull() {
            addCriterion("plan_product is not null");
            return (Criteria) this;
        }

        public Criteria andPlanProductEqualTo(Integer value) {
            addCriterion("plan_product =", value, "planProduct");
            return (Criteria) this;
        }

        public Criteria andPlanProductNotEqualTo(Integer value) {
            addCriterion("plan_product <>", value, "planProduct");
            return (Criteria) this;
        }

        public Criteria andPlanProductGreaterThan(Integer value) {
            addCriterion("plan_product >", value, "planProduct");
            return (Criteria) this;
        }

        public Criteria andPlanProductGreaterThanOrEqualTo(Integer value) {
            addCriterion("plan_product >=", value, "planProduct");
            return (Criteria) this;
        }

        public Criteria andPlanProductLessThan(Integer value) {
            addCriterion("plan_product <", value, "planProduct");
            return (Criteria) this;
        }

        public Criteria andPlanProductLessThanOrEqualTo(Integer value) {
            addCriterion("plan_product <=", value, "planProduct");
            return (Criteria) this;
        }

        public Criteria andPlanProductIn(List<Integer> values) {
            addCriterion("plan_product in", values, "planProduct");
            return (Criteria) this;
        }

        public Criteria andPlanProductNotIn(List<Integer> values) {
            addCriterion("plan_product not in", values, "planProduct");
            return (Criteria) this;
        }

        public Criteria andPlanProductBetween(Integer value1, Integer value2) {
            addCriterion("plan_product between", value1, value2, "planProduct");
            return (Criteria) this;
        }

        public Criteria andPlanProductNotBetween(Integer value1, Integer value2) {
            addCriterion("plan_product not between", value1, value2, "planProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductIsNull() {
            addCriterion("already_product is null");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductIsNotNull() {
            addCriterion("already_product is not null");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductEqualTo(Integer value) {
            addCriterion("already_product =", value, "alreadyProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductNotEqualTo(Integer value) {
            addCriterion("already_product <>", value, "alreadyProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductGreaterThan(Integer value) {
            addCriterion("already_product >", value, "alreadyProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductGreaterThanOrEqualTo(Integer value) {
            addCriterion("already_product >=", value, "alreadyProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductLessThan(Integer value) {
            addCriterion("already_product <", value, "alreadyProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductLessThanOrEqualTo(Integer value) {
            addCriterion("already_product <=", value, "alreadyProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductIn(List<Integer> values) {
            addCriterion("already_product in", values, "alreadyProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductNotIn(List<Integer> values) {
            addCriterion("already_product not in", values, "alreadyProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductBetween(Integer value1, Integer value2) {
            addCriterion("already_product between", value1, value2, "alreadyProduct");
            return (Criteria) this;
        }

        public Criteria andAlreadyProductNotBetween(Integer value1, Integer value2) {
            addCriterion("already_product not between", value1, value2, "alreadyProduct");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * 
     * 
     * @author wcyong
     * 
     * @date 2018-07-19
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}