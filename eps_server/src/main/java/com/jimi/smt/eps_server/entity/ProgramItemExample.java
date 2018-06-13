package com.jimi.smt.eps_server.entity;

import java.util.ArrayList;
import java.util.List;

public class ProgramItemExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ProgramItemExample() {
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

        public Criteria andAlternativeIsNull() {
            addCriterion("alternative is null");
            return (Criteria) this;
        }

        public Criteria andAlternativeIsNotNull() {
            addCriterion("alternative is not null");
            return (Criteria) this;
        }

        public Criteria andAlternativeEqualTo(Boolean value) {
            addCriterion("alternative =", value, "alternative");
            return (Criteria) this;
        }

        public Criteria andAlternativeNotEqualTo(Boolean value) {
            addCriterion("alternative <>", value, "alternative");
            return (Criteria) this;
        }

        public Criteria andAlternativeGreaterThan(Boolean value) {
            addCriterion("alternative >", value, "alternative");
            return (Criteria) this;
        }

        public Criteria andAlternativeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("alternative >=", value, "alternative");
            return (Criteria) this;
        }

        public Criteria andAlternativeLessThan(Boolean value) {
            addCriterion("alternative <", value, "alternative");
            return (Criteria) this;
        }

        public Criteria andAlternativeLessThanOrEqualTo(Boolean value) {
            addCriterion("alternative <=", value, "alternative");
            return (Criteria) this;
        }

        public Criteria andAlternativeIn(List<Boolean> values) {
            addCriterion("alternative in", values, "alternative");
            return (Criteria) this;
        }

        public Criteria andAlternativeNotIn(List<Boolean> values) {
            addCriterion("alternative not in", values, "alternative");
            return (Criteria) this;
        }

        public Criteria andAlternativeBetween(Boolean value1, Boolean value2) {
            addCriterion("alternative between", value1, value2, "alternative");
            return (Criteria) this;
        }

        public Criteria andAlternativeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("alternative not between", value1, value2, "alternative");
            return (Criteria) this;
        }

        public Criteria andSpecitificationIsNull() {
            addCriterion("specitification is null");
            return (Criteria) this;
        }

        public Criteria andSpecitificationIsNotNull() {
            addCriterion("specitification is not null");
            return (Criteria) this;
        }

        public Criteria andSpecitificationEqualTo(String value) {
            addCriterion("specitification =", value, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationNotEqualTo(String value) {
            addCriterion("specitification <>", value, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationGreaterThan(String value) {
            addCriterion("specitification >", value, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationGreaterThanOrEqualTo(String value) {
            addCriterion("specitification >=", value, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationLessThan(String value) {
            addCriterion("specitification <", value, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationLessThanOrEqualTo(String value) {
            addCriterion("specitification <=", value, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationLike(String value) {
            addCriterion("specitification like", value, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationNotLike(String value) {
            addCriterion("specitification not like", value, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationIn(List<String> values) {
            addCriterion("specitification in", values, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationNotIn(List<String> values) {
            addCriterion("specitification not in", values, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationBetween(String value1, String value2) {
            addCriterion("specitification between", value1, value2, "specitification");
            return (Criteria) this;
        }

        public Criteria andSpecitificationNotBetween(String value1, String value2) {
            addCriterion("specitification not between", value1, value2, "specitification");
            return (Criteria) this;
        }

        public Criteria andPositionIsNull() {
            addCriterion("position is null");
            return (Criteria) this;
        }

        public Criteria andPositionIsNotNull() {
            addCriterion("position is not null");
            return (Criteria) this;
        }

        public Criteria andPositionEqualTo(String value) {
            addCriterion("position =", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotEqualTo(String value) {
            addCriterion("position <>", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionGreaterThan(String value) {
            addCriterion("position >", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionGreaterThanOrEqualTo(String value) {
            addCriterion("position >=", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionLessThan(String value) {
            addCriterion("position <", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionLessThanOrEqualTo(String value) {
            addCriterion("position <=", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionLike(String value) {
            addCriterion("position like", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotLike(String value) {
            addCriterion("position not like", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionIn(List<String> values) {
            addCriterion("position in", values, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotIn(List<String> values) {
            addCriterion("position not in", values, "position");
            return (Criteria) this;
        }

        public Criteria andPositionBetween(String value1, String value2) {
            addCriterion("position between", value1, value2, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotBetween(String value1, String value2) {
            addCriterion("position not between", value1, value2, "position");
            return (Criteria) this;
        }

        public Criteria andQuantityIsNull() {
            addCriterion("quantity is null");
            return (Criteria) this;
        }

        public Criteria andQuantityIsNotNull() {
            addCriterion("quantity is not null");
            return (Criteria) this;
        }

        public Criteria andQuantityEqualTo(Integer value) {
            addCriterion("quantity =", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotEqualTo(Integer value) {
            addCriterion("quantity <>", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThan(Integer value) {
            addCriterion("quantity >", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("quantity >=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThan(Integer value) {
            addCriterion("quantity <", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("quantity <=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityIn(List<Integer> values) {
            addCriterion("quantity in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotIn(List<Integer> values) {
            addCriterion("quantity not in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityBetween(Integer value1, Integer value2) {
            addCriterion("quantity between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("quantity not between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andSerialNoIsNull() {
            addCriterion("serial_no is null");
            return (Criteria) this;
        }

        public Criteria andSerialNoIsNotNull() {
            addCriterion("serial_no is not null");
            return (Criteria) this;
        }

        public Criteria andSerialNoEqualTo(Integer value) {
            addCriterion("serial_no =", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoNotEqualTo(Integer value) {
            addCriterion("serial_no <>", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoGreaterThan(Integer value) {
            addCriterion("serial_no >", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoGreaterThanOrEqualTo(Integer value) {
            addCriterion("serial_no >=", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoLessThan(Integer value) {
            addCriterion("serial_no <", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoLessThanOrEqualTo(Integer value) {
            addCriterion("serial_no <=", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoIn(List<Integer> values) {
            addCriterion("serial_no in", values, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoNotIn(List<Integer> values) {
            addCriterion("serial_no not in", values, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoBetween(Integer value1, Integer value2) {
            addCriterion("serial_no between", value1, value2, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoNotBetween(Integer value1, Integer value2) {
            addCriterion("serial_no not between", value1, value2, "serialNo");
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