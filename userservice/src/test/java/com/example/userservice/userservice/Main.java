package com.example.userservice.userservice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] orders = {{4,3,1,2,5}, {5,4,3,2,1}};
        int[] results = {2, 5};

        for (int i = 0; i < 2; i++) {
            int[] order = orders[i];
            int result = results[i];

            int answer = solution.solution(order);
            System.out.println(answer);
            assertEquals(result, answer);
        }
    }

}

class Solution {

    public int solution(int[] order) {
        int answer = 0;
        Stack<Integer> stack = new Stack<>();

        for (int box = 1; box <= order.length; box++) {
            stack.push(box);
            while (!stack.isEmpty() && stack.peek() == order[answer]) {
                stack.pop();
                answer++;
            }
        }

        return answer;
    }

}