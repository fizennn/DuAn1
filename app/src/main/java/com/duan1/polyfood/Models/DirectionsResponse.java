package com.duan1.polyfood.Models;

import java.util.List;

public class DirectionsResponse {
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public class Route {
        private List<Leg> legs;

        public List<Leg> getLegs() {
            return legs;
        }

        public class Leg {
            private List<Step> steps;

            public List<Step> getSteps() {
                return steps;
            }

            public class Step {
                private String html_instructions;
                private Polyline polyline;

                public String getHtmlInstructions() {
                    return html_instructions;
                }

                public Polyline getPolyline() {
                    return polyline;
                }

                public class Polyline {
                    private String points;

                    public String getPoints() {
                        return points;
                    }
                }
            }
        }
    }
}

