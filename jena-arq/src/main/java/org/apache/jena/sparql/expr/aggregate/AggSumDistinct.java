/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.sparql.expr.aggregate;

import org.apache.jena.graph.Node ;
import org.apache.jena.sparql.engine.binding.Binding ;
import org.apache.jena.sparql.expr.Expr ;
import org.apache.jena.sparql.expr.ExprEvalException ;
import org.apache.jena.sparql.expr.ExprList ;
import org.apache.jena.sparql.expr.NodeValue ;
import org.apache.jena.sparql.expr.nodevalue.XSDFuncOp ;
import org.apache.jena.sparql.function.FunctionEnv ;

public class AggSumDistinct  extends AggregatorBase
{
    // ---- SUM(DISTINCT expr)
    public AggSumDistinct(Expr expr) { super("SUM", true, expr) ; } 
    @Override
    public Aggregator copy(ExprList exprs) { return new AggSumDistinct(exprs.get(0)) ; }

    private static final NodeValue noValuesToSum = NodeValue.nvZERO ; 
    
    @Override
    public Accumulator createAccumulator()
    { 
        return new AccSumDistinct(getExpr()) ;
    }

    @Override
    public int hashCode()   { return HC_AggSumDistinct ^ getExpr().hashCode() ; }
    
    @Override
    public boolean equals(Aggregator other, boolean bySyntax) {
        if ( other == null ) return false ;
        if ( this == other ) return true ; 
        if ( ! ( other instanceof AggSumDistinct ) )
            return false ;
        AggSumDistinct agg = (AggSumDistinct)other ;
        return getExpr().equals(agg.getExpr(), bySyntax) ;
    } 

    @Override
    public Node getValueEmpty()     { return NodeValue.toNode(noValuesToSum) ; } 

    // ---- Accumulator
    class AccSumDistinct extends AccumulatorExpr
    {
        // Non-empty case but still can be nothing because the expression may be undefined.
        private NodeValue total = null ;

        public AccSumDistinct(Expr expr) { super(expr, true) ; }

        @Override
        public void accumulate(NodeValue nv, Binding binding, FunctionEnv functionEnv)
        { 
            if ( nv.isNumber() )
            {
                if ( total == null )
                    total = nv ;
                else
                    total = XSDFuncOp.numAdd(nv, total) ;
            }
            else
                throw new ExprEvalException("Not a number: "+nv) ;
        }
        
        @Override
        public NodeValue getAccValue()
        { return total ; }

        @Override
        protected void accumulateError(Binding binding, FunctionEnv functionEnv)
        {}
    }
}
