package com.jimi.smt.eps_server.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperationExample {
    protected String orderByClause;

    protected boolean distinct;

    protected Integer limitStart;
    
    protected Integer limitSize;

    protected List<Criteria> oredCriteria;

    public OperationExample() {
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

    public Integer getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(Integer limitStart) {
		this.limitStart = limitStart;
	}

	public Integer getLimitSize() {
		return limitSize;
	}

	public void setLimitSize(Integer limitSize) {
		this.limitSize = limitSize;
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOperatorIsNull() {
            addCriterion("operator is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIsNotNull() {
            addCriterion("operator is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorEqualTo(String value) {
            addCriterion("operator =", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotEqualTo(String value) {
            addCriterion("operator <>", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorGreaterThan(String value) {
            addCriterion("operator >", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorGreaterThanOrEqualTo(String value) {
            addCriterion("operator >=", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorLessThan(String value) {
            addCriterion("operator <", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorLessThanOrEqualTo(String value) {
            addCriterion("operator <=", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorLike(String value) {
            addCriterion("operator like", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotLike(String value) {
            addCriterion("operator not like", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorIn(List<String> values) {
            addCriterion("operator in", values, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotIn(List<String> values) {
            addCriterion("operator not in", values, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorBetween(String value1, String value2) {
            addCriterion("operator between", value1, value2, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotBetween(String value1, String value2) {
            addCriterion("operator not between", value1, value2, "operator");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("time is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("time is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Date value) {
            addCriterion("time =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Date value) {
            addCriterion("time <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Date value) {
            addCriterion("time >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("time >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Date value) {
            addCriterion("time <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Date value) {
            addCriterion("time <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Date> values) {
            addCriterion("time in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Date> values) {
            addCriterion("time not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Date value1, Date value2) {
            addCriterion("time between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Date value1, Date value2) {
            addCriterion("time not between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andResultIsNull() {
            addCriterion("result is null");
            return (Criteria) this;
        }

        public Criteria andResultIsNotNull() {
            addCriterion("result is not null");
            return (Criteria) this;
        }

        public Criteria andResultEqualTo(String value) {
            addCriterion("result =", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotEqualTo(String value) {
            addCriterion("result <>", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThan(String value) {
            addCriterion("result >", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThanOrEqualTo(String value) {
            addCriterion("result >=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThan(String value) {
            addCriterion("result <", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThanOrEqualTo(String value) {
            addCriterion("result <=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLike(String value) {
            addCriterion("result like", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotLike(String value) {
            addCriterion("result not like", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultIn(List<String> values) {
            addCriterion("result in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotIn(List<String> values) {
            addCriterion("result not in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultBetween(String value1, String value2) {
            addCriterion("result between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotBetween(String value1, String value2) {
            addCriterion("result not between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andLineseatIsNull() {
            addCriterion("lineseat is null");
            return (Criteria) this;
        }

        public Criteria andLineseatIsNotNull() {
            addCriterion("lineseat is not null");
            return (Criteria) this;
        }

        public Criteria andLineseatEqualTo(String value) {
            addCriterion("lineseat =", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatNotEqualTo(String value) {
            addCriterion("lineseat <>", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatGreaterThan(String value) {
            addCriterion("lineseat >", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatGreaterThanOrEqualTo(String value) {
            addCriterion("lineseat >=", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatLessThan(String value) {
            addCriterion("lineseat <", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatLessThanOrEqualTo(String value) {
            addCriterion("lineseat <=", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatLike(String value) {
            addCriterion("lineseat like", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatNotLike(String value) {
            addCriterion("lineseat not like", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatIn(List<String> values) {
            addCriterion("lineseat in", values, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatNotIn(List<String> values) {
            addCriterion("lineseat not in", values, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatBetween(String value1, String value2) {
            addCriterion("lineseat between", value1, value2, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatNotBetween(String value1, String value2) {
            addCriterion("lineseat not between", value1, value2, "lineseat");
            return (Criteria) this;
        }

        public Criteria andMaterialNoIsNull() {
            addCriterion("material_no is null");
            return (Criteria) this;
        }

        public Criteria andMaterialNoIsNotNull() {
            addCriterion("material_no is not null");
            return (Criteria) this;
        }

        public Criteria andMaterialNoEqualTo(String value) {
            addCriterion("material_no =", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoNotEqualTo(String value) {
            addCriterion("material_no <>", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoGreaterThan(String value) {
            addCriterion("material_no >", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoGreaterThanOrEqualTo(String value) {
            addCriterion("material_no >=", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoLessThan(String value) {
            addCriterion("material_no <", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoLessThanOrEqualTo(String value) {
            addCriterion("material_no <=", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoLike(String value) {
            addCriterion("material_no like", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoNotLike(String value) {
            addCriterion("material_no not like", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoIn(List<String> values) {
            addCriterion("material_no in", values, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoNotIn(List<String> values) {
            addCriterion("material_no not in", values, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoBetween(String value1, String value2) {
            addCriterion("material_no between", value1, value2, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoNotBetween(String value1, String value2) {
            addCriterion("material_no not between", value1, value2, "materialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoIsNull() {
            addCriterion("old_material_no is null");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoIsNotNull() {
            addCriterion("old_material_no is not null");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoEqualTo(String value) {
            addCriterion("old_material_no =", value, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoNotEqualTo(String value) {
            addCriterion("old_material_no <>", value, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoGreaterThan(String value) {
            addCriterion("old_material_no >", value, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoGreaterThanOrEqualTo(String value) {
            addCriterion("old_material_no >=", value, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoLessThan(String value) {
            addCriterion("old_material_no <", value, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoLessThanOrEqualTo(String value) {
            addCriterion("old_material_no <=", value, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoLike(String value) {
            addCriterion("old_material_no like", value, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoNotLike(String value) {
            addCriterion("old_material_no not like", value, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoIn(List<String> values) {
            addCriterion("old_material_no in", values, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoNotIn(List<String> values) {
            addCriterion("old_material_no not in", values, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoBetween(String value1, String value2) {
            addCriterion("old_material_no between", value1, value2, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andOldMaterialNoNotBetween(String value1, String value2) {
            addCriterion("old_material_no not between", value1, value2, "oldMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanlineseatIsNull() {
            addCriterion("scanlineseat is null");
            return (Criteria) this;
        }

        public Criteria andScanlineseatIsNotNull() {
            addCriterion("scanlineseat is not null");
            return (Criteria) this;
        }

        public Criteria andScanlineseatEqualTo(String value) {
            addCriterion("scanlineseat =", value, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatNotEqualTo(String value) {
            addCriterion("scanlineseat <>", value, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatGreaterThan(String value) {
            addCriterion("scanlineseat >", value, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatGreaterThanOrEqualTo(String value) {
            addCriterion("scanlineseat >=", value, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatLessThan(String value) {
            addCriterion("scanlineseat <", value, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatLessThanOrEqualTo(String value) {
            addCriterion("scanlineseat <=", value, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatLike(String value) {
            addCriterion("scanlineseat like", value, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatNotLike(String value) {
            addCriterion("scanlineseat not like", value, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatIn(List<String> values) {
            addCriterion("scanlineseat in", values, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatNotIn(List<String> values) {
            addCriterion("scanlineseat not in", values, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatBetween(String value1, String value2) {
            addCriterion("scanlineseat between", value1, value2, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andScanlineseatNotBetween(String value1, String value2) {
            addCriterion("scanlineseat not between", value1, value2, "scanlineseat");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andProgramIdIsNull() {
            addCriterion("program_id is null");
            return (Criteria) this;
        }

        public Criteria andProgramIdIsNotNull() {
            addCriterion("program_id is not null");
            return (Criteria) this;
        }

        public Criteria andProgramIdEqualTo(String value) {
            addCriterion("program_id =", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdNotEqualTo(String value) {
            addCriterion("program_id <>", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdGreaterThan(String value) {
            addCriterion("program_id >", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdGreaterThanOrEqualTo(String value) {
            addCriterion("program_id >=", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdLessThan(String value) {
            addCriterion("program_id <", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdLessThanOrEqualTo(String value) {
            addCriterion("program_id <=", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdLike(String value) {
            addCriterion("program_id like", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdNotLike(String value) {
            addCriterion("program_id not like", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdIn(List<String> values) {
            addCriterion("program_id in", values, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdNotIn(List<String> values) {
            addCriterion("program_id not in", values, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdBetween(String value1, String value2) {
            addCriterion("program_id between", value1, value2, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdNotBetween(String value1, String value2) {
            addCriterion("program_id not between", value1, value2, "programId");
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
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

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