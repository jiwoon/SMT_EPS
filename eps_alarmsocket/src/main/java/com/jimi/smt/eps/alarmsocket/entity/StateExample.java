package com.jimi.smt.eps.alarmsocket.entity;

import java.util.ArrayList;
import java.util.List;

public class StateExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StateExample() {
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

        public Criteria andLineIsNull() {
            addCriterion("line is null");
            return (Criteria) this;
        }

        public Criteria andLineIsNotNull() {
            addCriterion("line is not null");
            return (Criteria) this;
        }

        public Criteria andLineEqualTo(Integer value) {
            addCriterion("line =", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineNotEqualTo(Integer value) {
            addCriterion("line <>", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineGreaterThan(Integer value) {
            addCriterion("line >", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineGreaterThanOrEqualTo(Integer value) {
            addCriterion("line >=", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineLessThan(Integer value) {
            addCriterion("line <", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineLessThanOrEqualTo(Integer value) {
            addCriterion("line <=", value, "line");
            return (Criteria) this;
        }

        public Criteria andLineIn(List<Integer> values) {
            addCriterion("line in", values, "line");
            return (Criteria) this;
        }

        public Criteria andLineNotIn(List<Integer> values) {
            addCriterion("line not in", values, "line");
            return (Criteria) this;
        }

        public Criteria andLineBetween(Integer value1, Integer value2) {
            addCriterion("line between", value1, value2, "line");
            return (Criteria) this;
        }

        public Criteria andLineNotBetween(Integer value1, Integer value2) {
            addCriterion("line not between", value1, value2, "line");
            return (Criteria) this;
        }

        public Criteria andAlarmingIsNull() {
            addCriterion("alarming is null");
            return (Criteria) this;
        }

        public Criteria andAlarmingIsNotNull() {
            addCriterion("alarming is not null");
            return (Criteria) this;
        }

        public Criteria andAlarmingEqualTo(Boolean value) {
            addCriterion("alarming =", value, "alarming");
            return (Criteria) this;
        }

        public Criteria andAlarmingNotEqualTo(Boolean value) {
            addCriterion("alarming <>", value, "alarming");
            return (Criteria) this;
        }

        public Criteria andAlarmingGreaterThan(Boolean value) {
            addCriterion("alarming >", value, "alarming");
            return (Criteria) this;
        }

        public Criteria andAlarmingGreaterThanOrEqualTo(Boolean value) {
            addCriterion("alarming >=", value, "alarming");
            return (Criteria) this;
        }

        public Criteria andAlarmingLessThan(Boolean value) {
            addCriterion("alarming <", value, "alarming");
            return (Criteria) this;
        }

        public Criteria andAlarmingLessThanOrEqualTo(Boolean value) {
            addCriterion("alarming <=", value, "alarming");
            return (Criteria) this;
        }

        public Criteria andAlarmingIn(List<Boolean> values) {
            addCriterion("alarming in", values, "alarming");
            return (Criteria) this;
        }

        public Criteria andAlarmingNotIn(List<Boolean> values) {
            addCriterion("alarming not in", values, "alarming");
            return (Criteria) this;
        }

        public Criteria andAlarmingBetween(Boolean value1, Boolean value2) {
            addCriterion("alarming between", value1, value2, "alarming");
            return (Criteria) this;
        }

        public Criteria andAlarmingNotBetween(Boolean value1, Boolean value2) {
            addCriterion("alarming not between", value1, value2, "alarming");
            return (Criteria) this;
        }

        public Criteria andConverypausedIsNull() {
            addCriterion("converyPaused is null");
            return (Criteria) this;
        }

        public Criteria andConverypausedIsNotNull() {
            addCriterion("converyPaused is not null");
            return (Criteria) this;
        }

        public Criteria andConverypausedEqualTo(Boolean value) {
            addCriterion("converyPaused =", value, "converypaused");
            return (Criteria) this;
        }

        public Criteria andConverypausedNotEqualTo(Boolean value) {
            addCriterion("converyPaused <>", value, "converypaused");
            return (Criteria) this;
        }

        public Criteria andConverypausedGreaterThan(Boolean value) {
            addCriterion("converyPaused >", value, "converypaused");
            return (Criteria) this;
        }

        public Criteria andConverypausedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("converyPaused >=", value, "converypaused");
            return (Criteria) this;
        }

        public Criteria andConverypausedLessThan(Boolean value) {
            addCriterion("converyPaused <", value, "converypaused");
            return (Criteria) this;
        }

        public Criteria andConverypausedLessThanOrEqualTo(Boolean value) {
            addCriterion("converyPaused <=", value, "converypaused");
            return (Criteria) this;
        }

        public Criteria andConverypausedIn(List<Boolean> values) {
            addCriterion("converyPaused in", values, "converypaused");
            return (Criteria) this;
        }

        public Criteria andConverypausedNotIn(List<Boolean> values) {
            addCriterion("converyPaused not in", values, "converypaused");
            return (Criteria) this;
        }

        public Criteria andConverypausedBetween(Boolean value1, Boolean value2) {
            addCriterion("converyPaused between", value1, value2, "converypaused");
            return (Criteria) this;
        }

        public Criteria andConverypausedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("converyPaused not between", value1, value2, "converypaused");
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